var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('configurationCtrl', function ($scope, $http, $mdDialog) {

	  $scope.beacons =   [
	    { id: 1, name: 'EC:E7:42:7C:AB:2B'},
	    { id: 2, name: 'F9:39:03:69:9B:B4'},
	    { id: 3, name: 'F4:1C:50:21:23:FD'},
	    { id: 4, name: 'D4:E0:02:F8:70:3C'},
	    { id: 5, name: 'C8:F1:74:81:5E:D3'}
	  ];
	  
	  
	  $scope.beacon = undefined;

	  $scope.beaconsConfigurations =  [];/* [ {
		  address : 'EC:E7:42:7C:AB:2B',
		  color : "#6699ff",
		  pos_x : 45,
		  pos_y : 145,
		  pos_z : 150,
		  description : 'beacon przy oknie...'
	  }];*/
	  
	  $scope.saveConfiguration = function() {
		  //console.log($scope.beaconsConfigurations)
		    $http({
		      method: 'POST',
		      url: '/saveConfiguration',
		      data: $scope.beaconsConfigurations,
		      headers: {'Content-Type': 'application/json'}
		    })
		      .success(function (data) {
			    showAlert("Zaktualizowano");
		        console.log("success");
		      })
		      .error(function () {
		        showAlert("Brak połączenia z serwerem");
		      })
	  }
	  
	  $scope.getConfiguration = function() {
		  $http.get('/getBeacons')
		  	.success(function(data) {
		  		$scope.beaconsConfigurations = data;
		  		//console.log(data);
		  	})
	      .error(function () {
		        showAlert("Brak połączenia z serwerem");
		      })
	  }
	  $scope.changeColor = function(index) {
		  var canvas, ctx;
		  canvas = document.getElementById("canvas2");
		  ctx = canvas.getContext("2d");
		  ctx.fillStyle = $scope.beaconsConfigurations[index].color;
		  ctx.fillRect(0, 0, canvas.width, canvas.height);
		    
	  }
	  $scope.getConfiguration();
	  
	  $scope.getAvailableBeacons = function () {
	    return $scope.beacons.filter(function (element) {
	      return $scope.beaconsConfigurations.findIndex(function(beacon) {
	    	  if(beacon.address == element.name) {
	    		  return true;
	    	  }
	      } ) < 0;
	    })
	  };
	  
	  $scope.addNewConfiguration = function(newConfiguration) {
		  if($scope.beacon==undefined || newConfiguration == undefined || newConfiguration.hasOwnProperty('color') == false || newConfiguration.hasOwnProperty('pos_x') == false || newConfiguration.hasOwnProperty('pos_y') == false 
				  || newConfiguration.hasOwnProperty('pos_z') == false || newConfiguration.hasOwnProperty('description') == false) {
			  showAlert("Proszę wypełnić wszystkie pola");
				}
		  else {
			  newConfiguration.address = $scope.beacon.name;
			  $scope.beaconsConfigurations.push({
				  address : newConfiguration.address,
				  color : newConfiguration.color,
				  pos_x : newConfiguration.pos_x,
				  pos_y : newConfiguration.pos_y,
				  pos_z : newConfiguration.pos_z,
				  description : newConfiguration.description
			  });  
		  }
	  }
	  
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
	  
	  
});