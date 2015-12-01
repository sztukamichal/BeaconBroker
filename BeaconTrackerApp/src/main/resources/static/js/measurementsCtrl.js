/**
 * Created by User on 2015-11-30.
 */

var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('measurementsCtrl', function ($scope, $http, NgTableParams, $filter, $mdDialog) {
	$scope.getDatasource = [
	                        { id: -30, title: "-30"},
	                        { id: -20, title: "-20"},
	                        { id: -16, title: "-16"},
	                        { id: -12, title: "-12"},
	                        { id: -8, title: "-8"},
	                        { id: -4, title: "-4"},
	                        { id: 0, title: "0"}, 
	                        { id: 4, title: "4"}];
	      
	$scope.done = false;
	$scope.openMenu = function($mdOpenMenu, ev) {
	      originatorEv = ev;
	      $mdOpenMenu(ev);
};
	$scope.selectedIndex = 0;
	 $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
	  $scope.series = ['Series A'];
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40]
	  ];
	$scope.ddd =[];
	$http.get('/getMeasurements')
                  .success(function (data) {
                	  $scope.data = data;
                	  $scope.done = true;
                	    $scope.tableParams = new NgTableParams({
                	        page: 1,            // show first page
                	        count: 10          // count per page
                	    }, {
                	        total: $scope.data.length, // length of data
                	        getData: function($defer, params) {
                	            // use build-in angular filter

                                var orderedData = params.filter() ?
                                        $filter('filter')(data, params.filter()) :
                                            data;
                                orderedData = params.sorting() ?
                                            $filter('orderBy')(orderedData, params.orderBy()) :
                                            orderedData;
                                $scope.ddd = orderedData;            
                                $scope.labels = $scope.ddd.map(function(element) {
                                	return element.txPower;
                                });
                                $scope.data = [$scope.ddd.map(function(element) {
                                	return 0 - element.rssi;
                                })];
	            	            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                	        }
                	      });
                    })
                    .error(function () {
                    	console.log("Błąd")
                    	$scope.done = true;
                    	$scope.showAlert("Brak komunikacji z serwerem...")
                    });
      $scope.getDate = function(time) {
    	  var date = new Date(time);
    	  return date.customFormat( "#DD#/#MM#/#YYYY# #hh#:#mm#:#ss#" );
      }
      
      $scope.showAlert = function(text) {
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