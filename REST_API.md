# REST API

### Action: get all meals from current user.

    'GET http://localhost:8080/topjava_war_exploded/rest/meals'
    curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals/{id}'
    
### Action: create a new meal from current user.

    'POST http://localhost:8080/topjava_war_exploded/rest/meals'
    curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals' \
    --form 'dateTime="2020-12-01T12:59:59"' \
    --form 'calories="999"' \
    --form 'description="STRINGVALUE"'
  
### Action: delete meal with id=100002 from current user.

    curl --location --request DELETE 'http://localhost:8080/topjava_war_exploded/rest/meals/100002'
    
### Action: update meal with id=100002 from current user.

    curl --location --request PUT 'http://localhost:8080/topjava_war_exploded/rest/meals/100002' \
    --header 'Cookie: JSESSIONID=50E3F24F843AD408619BB6AF30C0A393' \
    --form 'dateTime="2020-12-01T12:59:59"' \
    --form 'calories="666"' \
    --form 'description="Updated"' \
    --form 'id="100002"'

### Action: get meal with id=100002 from current user.

    curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals/100002' \
    --header 'Cookie: JSESSIONID=50E3F24F843AD408619BB6AF30C0A393'