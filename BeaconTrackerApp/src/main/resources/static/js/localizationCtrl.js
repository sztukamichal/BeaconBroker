
var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('localizationCtrl', function($scope, $http, $interval, $mdDialog, $location) {

  $scope.beaconsConfigurations =  [];/* [ {
  address : 'EC:E7:42:7C:AB:2B',
  color : "#6699ff",
  pos_x : 45,
  pos_y : 145,
  pos_z : 150,
  description : 'beacon przy oknie...'
}];*/
  $scope.devices = [];
  $scope.isServerResponding = false;
  $scope.errorWithConnection = false;
  var respFactor = null;
  var respFactor = null;
  $scope.numOfMeasurements = 5;
  $scope.error = 0;
  $scope.real_pos_x = 0;
  $scope.real_pos_y = 0;
  var timeOfLastUpdate = 0;
  
  var canvas, ctx, maxX, maxY;
  
  function showPos()
	{
	  	canvas = document.getElementById("respondCanvas");
		ctx = canvas.getContext("2d");
		ctx.clearRect(0, 0, canvas.width, canvas.height);
	
		var x = canvas.width;
		var y = canvas.height;  	
		
		
		respFactor = (x-40)/(maxX);
		respFactor = respFactor >  (y-40)/(maxY) ? (y-40)/(maxY) : respFactor;
		

		ctx.fillStyle = "#DDDDDD"; //black
		ctx.fillRect( 0, 0, x, y); //fill the canvas
	
		var elements = [];
		var distancesElements = [];
		var i = 0;
		$scope.beaconsConfigurations.forEach(function(element) {
			if(element.r != 0) {
				elements[i] = [];
				elements[i][0] = element.pos_x/100;
				elements[i][1] = element.pos_y/100;
				elements[i][2] = element.pos_z/100;
				distancesElements[i] = element.r;
				i++;
			}
		});
		var distances;
		var anchors;
		var pos = null;
		if(elements.length > 2 && distancesElements.length > 2 ) {
			console.log(elements);
			console.log(distancesElements);
			
			distances = Vector.create(distancesElements);
			anchors = $M(elements);
			pos = null;
			pos = lls_positioning(anchors, distances);
			$scope.position = pos;
			if(pos[0] > maxX/100) {
				pos[0] = maxX/100;
			} else if(pos[0] < 0) {
				pos[0] = 0;
			}
			if(pos[1] > maxY/100) {
				pos[1] = maxY/100;
			} else if(pos[1] < 0) {
				pos[1] = 0;
			}
		}

		$scope.beaconsConfigurations.forEach(function(element) {
			ctx.fillStyle = element.color;
			ctx.strokeStyle = ctx.fillStyle;
			ctx.fillRect(20 + element.pos_x *respFactor - 2, 20 + element.pos_y *respFactor - 2, 5, 5);
			ctx.beginPath();
			ctx.arc(20 + element.pos_x *respFactor, 20 + element.pos_y *respFactor, element.r * 100 *((respFactor+respFactor)/2), 0, 2 * Math.PI);
			ctx.rect(18,18,maxX*respFactor,maxY*respFactor);
			ctx.stroke();
		});
		
		if (pos !== null)
		{
			ctx.fillStyle = "#47e339";
			ctx.fillRect(20 + pos[0]*100 *respFactor - 2, 20 + pos[1] *100 *respFactor - 2, 7, 14);	
			$scope.error = ($scope.real_pos_x - pos[0]) * ($scope.real_pos_x - pos[0]);
			console.log($scope.error)
			$scope.error += ($scope.real_pos_y - pos[1]) * ($scope.real_pos_y - pos[1]);
			console.log($scope.error)
			$scope.error = Math.sqrt($scope.error);
			console.log($scope.error)
		}
		
	}
  
  
  $scope.getConfiguration = function() {
	  $http.get('/getBeacons')
	  	.success(function(data) {
	  		$scope.beaconsConfigurations = data;
	  		if(data.length < 3) {
	  			showAlert("Proszę skonfigurować co najmniej trzy beacon'y");
	  		    $location.path('/configuration').replace();
	  		} else {
	  			$scope.enable = true;
	  			var c = $('#respondCanvas');
	  			maxX = $scope.beaconsConfigurations[0].pos_x;
	  			maxY = $scope.beaconsConfigurations[0].pos_y;
	  			$scope.beaconsConfigurations.forEach(function(element) {
	  				if(element.pos_x > maxX) {
	  					maxX = element.pos_x;
	  				}
	  				if(element.pos_y > maxY) {
	  					maxY = element.pos_y;
	  				}
	  				element.numOfReading = 0;
	  				element.r = 0;
	  				element.sumOfDistances = 0;
	  			});
	  		}
	  		showPos();
	  	})
      .error(function () {
	        showAlert("Brak połączenia z serwerem");
	  })
  }
  $scope.getConfiguration();
  
  
  $interval(function () {
    if($scope.errorWithConnection !== true) {
      updateDevicesInRange()
    }
  },50);
  
  $http.get("/getDevicesInRange")
  .success(function (data) {
    timeOfLastUpdate = data[0].lastUpdated.lastActivity;
  })
  .error(function (data) {
  })
  
  function updateDevicesInRange() {
	    $http.get("/getDevicesInRange")
	      .success(function (data) {
	        $scope.errorWithConnection = false;
	        $scope.isServerResponding = true;
	        $scope.devices = data;
	        if(timeOfLastUpdate != data[0].lastUpdated.lastActivity) {
	            timeOfLastUpdate = data[0].lastUpdated.lastActivity;
	        	updateRange($scope.devices[0].lastUpdated);
	        }
	        if($scope.devices.length === 0) {
	          $scope.errorMessage = "Nie znaleziono żadnych urządzeń w pobliżu"
	        } else {
	          $scope.errorMessage = "";
	          
	        }
	      })
	      .error(function (data) {
	        showAlert("Brak połączenia z serwerem");
	        $scope.isServerResponding = true;
	        $scope.errorWithConnection = true;
	        $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
	      })
	  }
  function updateRange(beacon) {
	  $scope.beaconsConfigurations.find(function(element) {
			if(beacon.address == element.address) {
				if(beacon.txPower != -30){
					element.numOfReading++;
					element.sumOfDistances += $scope.getDistance(beacon);
					if(element.numOfReading >= $scope.numOfMeasurements) {
						element.numOfReading = 0;
						element.r = element.sumOfDistances/$scope.numOfMeasurements;
						element.sumOfDistances =0;
						showPos();
					}
				}
				return true;
			}
		});
  }

  $scope.getDistance = function(beacon) {
	  var factors = $scope.factors.find(function (element) {
	      if(element.txPower == beacon.txPower) {
	        return true;
	      }
	    });
	  var exponent = -(beacon.rssi - factors.a)/(10*factors.n);
	  var distance = Math.pow(10, exponent);
	  if(distance > 5) {
		  distance = 5;
	  }
	  return distance;
  }
  
  $scope.factors = [ 
                    {
                    	txPower : -30,
                    	a : -95.373,
                    	n : 1.0822149937
                    }, 
                    {
                    	txPower : -20,
                    	a : -83.2170,
                    	n : 1.3313547008
                    }, 
                    {
                    	txPower : -16,
                    	a : -79.455,
                    	n : 1.4167806077
                    }, 
                    {
                    	txPower : -12,
                    	a : -75.556,
                    	n : 1.4319776693
                    }, 
                    {
                    	txPower : -8,
                    	a : -72.09,
                    	n : 1.4163200907
                    }, 
                    {
                    	txPower : -4,
                    	a :  -68.338,
                    	n : 1.4361223225
                    }, 
                    {
                    	txPower : 0,
                    	a : -64.443,
                    	n : 1.4133267301

                    }, 
                    {
                    	txPower : 4,
                    	a : -62.07,
                    	n : 1.4745754936
                    },
                    ];
  
  var showAlert = function(text) {
    $mdDialog.show(
      $mdDialog.alert()
        .parent(angular.element(document.querySelector('#popupContainer')))
        .clickOutsideToClose(true)
        .title(text)
        .ok('OK')
        .targetEvent(null)
    );
  };
  

	$(document).ready( function(){
		
		//Get the canvas & context
		var c = $('#respondCanvas');
		var ct = c.get(0).getContext('2d');
		var container = $(c).parent();
		
		//Run function when browser  resize
	  	$(window).resize( respondCanvas );
	  	
	  	function respondCanvas(){
			c.attr('width', $(container).width() ); //max width
			c.attr('height', $(container).height() ); //max height
			
			//Redraw & reposition content
			var x = c.width();
			var y = c.height();  			
			ct.font = "20px Calibri";
			
			ct.fillStyle = "#DDDDDD"; //black
			ct.fillRect( 0, 0, x, y); //fill the canvas
			if(respFactor != null) {
				showPos();
			}
		}
		
		//Initial call
		respondCanvas();
	});
  
});