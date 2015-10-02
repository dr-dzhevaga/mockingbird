[![Build Status](https://travis-ci.org/dr-dzhevaga/mockingbird.svg?branch=develop)](https://travis-ci.org/dr-dzhevaga/mockingbird)

# What is mockingbird #
Mockingbird is a tool for stubbing/mocking of web services (REST/SOAP/etc) over HTTP(S) protocol with flexible configuration and dynamic responses.

# Features #
* Flexible request-response mapping based on request parameters (method, uri, query parameters, headers, content)
* Request content parsing (Xpath, JSONPath, Regex)
* Dynamic response content based on jsp-like template (JSP-like macros + JavaScript) with access to request parameters and parsing result

# How to start #
1. Mockingbird works with jre 1.8. Please check your jre version before you start.
2. Download the archive.
3. Execute start.bat for Windows or start.sh for Linux.
4. Navigate to [http://localhost:8080/hello-world](http://localhost:8080/hello-world) in a browser to get the stubbed response.

# Settings #
Settings is organized as a tree and can be loaded from YAML or JSON file (see [Command-line options](#command-line-options)).
The settings root has a ```mapping``` child to define a request-response mapping and a ```parsing``` child to specify global parsing (see [Parsing](#parsing)):
```yaml
mapping: []
parsing: {}
```

## Mapping ##
Mapping is used to map a response to a request. 
When a request is received it is compared with patterns in mapping until the first match.
When a matched pattern is found the response from the same mapping is used to respond.
Also a mapping can have an optional ```parsing``` child to specify request-specific parsing (see [Parsing](#parsing)).
```yaml
mapping: 
  - 
    request: {}
    response: {}
  - 
    request: {}
    response: {}
    parsing: {}
```

### Request ###
A pattern to specify the request parameters. Has the following children:

* method 	
    * Can be any of the following: GET, POST, PUT, etc. 
    * Can be an array that means any of.
    * Case-insensitive. 	
    * Optional, default is GET.

	Example:
	```yaml
	request:
	  method: GET
	```
	```yaml
	request:
	  method: [PUT, POST]
	```
	```yaml
	request:
	  method: Delete
	```
* uri
    * A regular expression for matching the url
    * Host and port are ignored (i.e. /hello-world for http://localhost:8080/hello-world)
    * A query is ignored (i.e. /hello-world for /hello-world?query)
    * Optional, default is .* (any uri)

	Example:

	```yaml
	request:
	  uri: /user/\d{7}
	```

	```
	/user/8728240			- matches
	/user/account/6765478	- not matches
	user/8728240			- not matches
	```

* query
    * Specify the query parameters values
	* A value can be a regular expression
    * Optional, query parameter not specified in pattern can have any value
    * Order doesn't matter
	
	Example:
	
	```yaml
	  uri: /search
	  query:
	    sortBy: fresh
		sortDir: desc|asc
		rows: \d{1-2}
	```

	```
	/search?sortBy=fresh&sortDir=desc&rows=8			- matches
	/search?sortBy=fresh&sortDir=asc&rows=25			- matches
	/search?sortDir=desc&rows=7&sortBy=fresh			- matches
	/search?sortBy=rating&sortDir=asc&rows=25			- not matches
	/search?sortBy=fresh&rows=25						- not matches
	/search?sortBy=fresh&sortDir=desc&category=action	- matches
	```
* header
    * Specify the headers values
    * A value can be a regular expression
    * Optional, header not specified in pattern can have any value
    * Order doesn't matter
	
	Example:
	
	```yaml
	  header:
	    Content-Type: application/json
		Authorization: Basic [a-zA-Z]+==
	```

	```
	Content-Type: application/json						- matches
	Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==

	Content-Type: application/json						- not matches

	Content-Type: application/xml						- not matches
	Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
	```
* content
    * Specify the request content parsing result (see [Parsing](#parsing))
    * A value can be a regular expression
    * Optional
    * Order doesn't matter
	
	Example:
	
	```yaml
	parsing: 
	  JSONPath: 
	    name: user.name
        id: user.id
	request: 
	  content: 
		name: John
		id: \d{7}
	```	
	
	```javascript
	/* matches */
	{						
	   "user":{
		  "name":"John",
		  "id":"5486348"
	   }
	}
	/* not matches */
	{						
	   "user":{
		  "name":"John",
		  "id":"98EA89F8"
	   }
	}
	/* matches */
	{						
	   "user":{
		  "name":"John",
		  "id":"98EA89F8",
		  "age":27
	   }
	}
	/* not matches */
	{						
	   "user":{
		  "name":"John",
		  "age":27
	   }
	}
	```
### Response ###
A response to the request. Has the following children:
* status
    * Specify a status code
    * Optional, default is 200
* header
    * Specify headers
    * Optional, default is empty
* content
    * Specify the response content
    * If a valid file path is specified then the file content is used
    * Support jsp-like template (see [Jsp-like template](#jsp-like-template))
	* Optional, default is empty
	
	Example:

	```yaml
		response:
		  content: 638976839698
	```
	```yaml
		response:
		  status: 400
		  header:
			Content-Type: plain/text
		  content: Id can't be null
	```
	```yaml		
		response:
		  header:
			Content-Type: application/xml
		  content: templates/data.xml
	```

### Parsing ###
Defines request content parsing. Parsing result can be used for request matching (see [Request](#request)) and can be propagated to response (see [Jsp-like template](#jsp-like-template)).

Parsing can be defined for a specific request or for all requests:
* Parsing in the settings root is global and applied to all requests. 
* Parsing in the mapping is request-specific.
* Global parsing result can be used for request matching (see [Request](#request)).
* Both global and request-specific parsing result can be used in jsp-like template (see [Jsp-like template](#jsp-like-template))

The following parsing mechanisms are supported:

* XPath
    * Allows to parse the request content by a xpath
    * If the request content is not a valid xml then no parsing will be applied and result will be empty
    * If the xpath return an array then the first element is used as the result
* JSONPath
    * Allows to parse the request content by a jsonpath (see [JsonPath](http://goessner.net/articles/JsonPath/))
    * If the request content is not a valid json then no parsing will be applied and result will be empty
* Regex
    * Allows to parse the request content by a regular expression
    * if the regular expression contains groups then the first matched group is used as the result

Example:

```yaml
parsing:
  XPath:
    operation: local-name(soap:Envelope/soap:Body/*)
  JSONPath:
    name: user.name
    id: user.id
  Regex:
    date: (?<=date\s{0,1}=\s{0,1})\d{2}/\d{2}/\d{4}
	age: age\s*=\s*(\d+)
```

Parse result for the example has the parameter ```operation``` with value ```GetPrice``` in case of the following request content:
```xml	
<soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope">
    <soap:Body>
        <m:GetPrice xmlns:m="http://www.w3schools.com/prices">
            <m:Item>Apples</m:Item>
        </m:GetPrice>
    </soap:Body>
</soap:Envelope>
```
Parse result for the example has the parameter ```name``` with value ```John``` and the parameter ```id``` with value ```5486348``` in case of the following request content:
```json
{
   "user":{
      "name":"John",
      "id":"5486348"
   }
}
```
Parse result for the example has the parameter ```date``` with value ```08/03/1989``` for the request content and the parameter ```age``` with value ```26``` in case of the following request content:
```
date = 08/03/1989
age = 26
```

## Jsp-like template ##
Allows to make the response content dynamic. 

* Jsp-like macros: <%= %> for macro and <% %> for script
* JavaScript is used inside the macros ([Java 8 Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html))
* Request parameters and parsing result are accessible through JavaScript objects ```request``` and ```parsing```
  
  * ```request``` has the following fields:
  
      * method - string
      * uri - string
      * query - map (object) of arrays
      * header - map (object)
      * content - string
	
  * ```parsing``` has the same fields as parsing entries names.

Example:

In the following example ```id``` is propagated from the request to the response:
```yaml
parsing:
  JSONPath:
    id: user.id
response: 
  content: >
    {                       
      "user":{
        "id": "<%=parsing.id%>"
        "status": "active"
      }
    }
```

You can also make some processing inside a macro. In the next example all spaces in phone number are removed and the first number is replaced with +7:
```jsp 
+7<%=parsing.number.replace(/ /g,"").substring(1)%>
```

The following example demonstrate use of different request parameters:
```jsp 
Method: <%=request.method%>
All headers: <%=request.header%>
"Accept" header: <%=request.header.Accept%>
"Content-Type" header: <%=request.header['Content-Type']%>
All query parameters: <%=request.query%>
All "id" query parameters: <%=request.query.id%>
Query parameters number: <%=request.query.id.length%>
The first "id" query parameter: <%=request.query.id[0]%>
```

You can use JS and even Java to make your stub smarter:
```jsp 
Time: <%=new Date()%>
UUID: <%=java.util.UUID.randomUUID()%>
```

Script macro allows to add some section to a response content in case of some condition. In the following example "Discount" section is added to XML only if "Apples" product is requested:
```jsp 
<soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope">
    <soap:Body>
        <m:GetPriceResponse xmlns:m="http://www.w3schools.com/prices">
            <m:Price>15144</m:Price>
			<%if(parsing.product == 'Apples') {%>
			<m:Discount>20</m:Discount>
			<%}%>
        </m:GetPriceResponse>
    </soap:Body>
</soap:Envelope>
```

You can use cycles to generate a dynamic number of content entities:
```jsp 
<%number = request.query.number[0]%>
{
   <%for(i = 0; i < number; i++) {%>"user":{
      "id": <%=java.util.UUID.randomUUID()%>
   }<%}%>
}
```

You can iterate through maps:
```jsp 
<%for(name in request.header)%><%=name + ' : ' + request.header[name]%>
```

## Command-line options ##

Usage: java mockingbird.jar [-?] [-d] -f <file> -ff <JSON|YAML> -p <port>

 -?,--help                       print this message

 -d,--debug                      enable debug mode

 -f,--file <file>                specify settings file

 -ff,--file-format <JSON|YAML>   specify settings file format

 -p,--port <port>                specify server port