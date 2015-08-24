var express = require ('express');
var http =require('http');
var url =require('url');
var bodyParser= require('body-parser');
var app = express();
var mysql =require('mysql');

app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

app.all('*', function(req, res, next){
	res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'X-Requested-With, Authorization, Content-Type');
  next();
  });

app.listen(5581);


DATABASE_HOST = 'localhost';
DATABASE_NAME = 'HealthAlert';
DATABASE_USERNAME = 'root';
DATABASE_PASSWORD = ' ';

var connection = mysql.createConnection({
	                host :DATABASE_HOST,
	                user :DATABASE_USERNAME,
	                password :DATABASE_PASSWORD,
	                database :DATABASE_NAME,
});


app.get('/login', function(request, response) {

    var req = request.query;
    var email = req.email;
    var pass = req.pass;
    console.log(email);
    console.log(pass);
    
    var checkExistingQuery = "SELECT * FROM User_data WHERE Email = \'" + email + "\' and Pass = \'" + pass + "\';";

    connection.query(checkExistingQuery, function(errExistingUser, resExistingUser) {
        if (errExistingUser) {
            // throw err;

            response.json("Error: " + errExistingUser);
        } else {
            console.log(resExistingUser.length);
            
            if (resExistingUser.length == 0)
            {
             
             response.json(0);
            }
      
            else {
                response.json(resExistingUser[0]);
            }
        }
          
           
      });

});

app.get('/refresh', function(request, response) {

    
    var checkExistingQuery = "SELECT Topic,Story,Email,Lat,Longi FROM Alert";

    connection.query(checkExistingQuery, function(errExistingUser, resExistingUser) {
        if (errExistingUser) {
            // throw err;

            response.json("Error: " + errExistingUser);
        } else {
            console.log(resExistingUser.length);
            
            if (resExistingUser.length == 0)
            {
             
             response.json(0);
            }
      
            else {var i=0;
            		var j=resExistingUser.rowsAffected;
            	
            
                
                response.json(resExistingUser);
            }
        }
          
           
      });

});



 app.get('/signup', function(request, response) {

 		var req=request.query;
   	   var name = req.Name;
      
       var email = req.email;
       var pass=req.pass;
    
    var checkExistingQuery = "SELECT * FROM User_data WHERE Email = \'" + email + "\';";

    connection.query(checkExistingQuery, function(errExistingUser, resExistingUser) {
        if (errExistingUser) {
            // throw err;

            response.json("Error: " + errExistingUser);
        } 
        else {
            console.log(resExistingUser.length);
            
            if (resExistingUser.length == 0) {
                var addUserQuery = "INSERT INTO User_data(Email,Name,Pass) VALUES(\'"+email+"\',\'"+name+"\',\'"+pass+"\');";
                // var checkQuery ="SELECT user_id FROM user WHERE email_user = \'"+email+"\';";

                connection.query(addUserQuery, function(errNewUser, resNewUser) {
                    if (errNewUser) {
                        // throw errAddEmail;
                        response.json("Error: " + errNewUser);
                    } else {
                        //console.log(resNewUser.insertId);
                       
                        response.json("database updated");
                    }
                });

            } else {

            response.json(0);
                
            }
        }
          
           
      });

});


 app.get('/post', function(request, response) {

       var req = request.query;
       var topic = req.topic;
       var story=req.story;
       var email=req.email;
       var lat=req.lat;
       var longi=req.longi;

       console.log(story);
       
  
  var addUserInfo = "INSERT INTO Alert(Topic,Story,Email,Lat,Longi) VALUES(\'"+topic+"\',\'"+story+"\',\'"+email+"\',\'"+lat+"\',\'"+longi+"\');";
       connection.query(addUserInfo, function(errUserInfo, resUserInfo) {
           if (errUserInfo) {
                // throw err;
                response.json("Error: " + errUserInfo);
           } else {
                 console.log(resUserInfo);
                     //console.log(resNewUser.insertId);
                        var newUserInfoJSON = {};
                        newUserInfoJSON['rowsAffected'] = resUserInfo.affectedRows;
                        response.json(newUserInfoJSON);
        }
        });
          
          
      });
 
