/**
 * Created by User on 2015-11-30.
 */

var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('measurementsCtrl', function ($scope, $http, NgTableParams, $filter, $mdDialog, $q) {

  //TABELA
  $scope.getTxPowersToSelect = function () {
    var def = $q.defer();
    $http.get('/getTxPowers')
      .success(function (data) {
        def.resolve(data);
      });
    return def;
  };
  $scope.getDistancesToSelect = function () {
    var def = $q.defer();
    $http.get('/getDistances')
      .success(function (data) {
        def.resolve(data);
      });
    return def;
  };

  $scope.done = true;
  $scope.openMenu = function ($mdOpenMenu, ev) {
    originatorEv = ev;
    $mdOpenMenu(ev);
  };
  $scope.dateFormat = 'yyyy-MM-dd';
  $scope.selectedIndex = 0;

  //wykresy
  {
    $scope.chartLabels = [];
    $scope.chartSeries = ['Series A'];
    $scope.chartData = [
      []
    ];
  }

  $scope.getStatisticData = function() {

    if($scope.data !== undefined && $scope.data.length > 0) {
      var data = $filter('orderBy')($scope.data, {distance: 'asc'});
      $scope.data.sort(function(a, b){return a.distance-b.distance});
      data = $scope.data;
      var counter = 0;
      var adder = 0;
      var result = [];
      var averagesRssi = [];
      var currentDistance = data[0].distance;
      var iterator = 0;
      data.forEach(function (element) {
        if (currentDistance != element.distance) {
    		var avgRssi = adder/counter;
        	console.log("Dystans " + currentDistance + " średnia " + avgRssi);
        	iterator = averagesRssi.findIndex(function (element) {
                if(element.distance == currentDistance) {
                    return true;
                  }
                });
        	console.log(averagesRssi.length);
        	console.log("Iterator : " + iterator);
        	if(iterator < 0) {
            	averagesRssi.push( 
            		  {
            			  counter : counter,
            			  avgRssi : avgRssi,
    		        	  txPower : element.txPower,
    		        	  distance : currentDistance
    		        	 }
            		  );	
        	}
        	else {
        		var old = averagesRssi[iterator].avgRssi * averagesRssi[iterator].counter;
        		var newOne =  avgRssi *counter;
        		averagesRssi[iterator].avgRssi = ( old + newOne )/(averagesRssi[iterator].counter + counter);
        		averagesRssi[iterator].counter = averagesRssi[iterator].counter + counter;
        	}
          counter = 0;
          adder = 0;
          currentDistance = element.distance;
        }
        adder += element.rssi;
        counter += 1;
      });
      var diffs = [];
      console.log(averagesRssi);
      
      currentDistance = data[0].distance;
      iterator = averagesRssi.findIndex(function (element) {
          if(element.distance == currentDistance) {
              return true;
            }
          });
      diffs = data.map(function (element) {
        if (currentDistance != element.distance) {
          iterator = averagesRssi.findIndex(function (element) {
              if(element.distance == currentDistance) {
                  return true;
                }
              });
          currentDistance = element.distance;
          }
        var diff = element.rssi - averagesRssi[iterator].avgRssi;
        var sqr = diff * diff;
        return {diff : sqr, distance : element.distance};
      });

      iterator = 0;
      adder = 0;
      counter = 0;
      var currentDistance = data[0].distance;
      diffs.forEach(function (element) {
          if (currentDistance != element.distance) {
        	var stdDev = Math.sqrt((adder/counter));
        	iterator = averagesRssi.findIndex(function (element) {
                if(element.distance == currentDistance) {
                    return true;
                  }
                });
        	var index = result.findIndex(function (element) {
                if(element.distance == currentDistance) {
                    return true;
                  }
                }); 
            if(index < 0 ) {
            	result.push(
            		{
            			txPower: averagesRssi[iterator].txPower,
            			distance: currentDistance,
            			avgRssi: averagesRssi[iterator].avgRssi,
            			stdDev : stdDev,
            			counter : counter
        			}
        		);
            } else {
        		var old = result[index].stdDev * result[index].counter;
        		var newOne =  stdDev *counter;
            	result[index].stdDev = (old + newOne)/(result[index].counter+counter);
        		result[index].counter = result[index].counter + counter;
            }
            counter = 0;
            adder = 0;
            currentDistance = element.distance;
          }
          adder += element.diff;
          counter += 1;
        }
      );
      console.log(result);
      $scope.statisticTableParams = new NgTableParams({
          page: 1,
          count: 10
        },
        {
          total: result.length,
          getData : function($defer, params) {

            var data = result;
            var filters = params.filter(),
              exactFilters = {};
            if (filters.hasOwnProperty('distance')) {
              exactFilters.distance = filters.distance;
              delete filters.distance;
            }
            var orderedData = params.filter() ?
              $filter('filter')(data, filters) :
              data;
            orderedData = exactFilters ?
              $filter('filter')(orderedData, exactFilters, true) :
              orderedData;
            orderedData = params.sorting() ?
              $filter('orderBy')(orderedData, params.orderBy()) :
              orderedData;
            filters.distance = exactFilters.distance;

            $scope.temp = orderedData;
            $scope.temp = $filter('orderBy')(orderedData, {distance: 'asc'});
            $scope.chartLabels = $scope.temp.map(function (element) {
              return element.distance;
            });
            $scope.chartData = [$scope.temp.map(function (element) {
              return element.avgRssi;
            })];
            $scope.statisticData = orderedData;
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          }
        });
    }
  };

  //Pobieranie z wymiaru czasowego
  {
    $scope.myDateFrom = new Date();
    $scope.myDateTo = new Date();
    $scope.myTimeFrom = new Date();
    $scope.myTimeTo = new Date();
    $scope.showDateRange = false;
  }

  $scope.getDataForDateRange = function () {
    if ($scope.myDateFrom == undefined || $scope.myTimeTo == undefined || $scope.myTimeFrom == undefined || $scope.myDateTo == undefined) {
      $scope.showAlert("Proszę wybrać zakres czasowy")
    } else {
      $scope.done = false;
      $scope.fromDate = $scope.myDateFrom.customFormat('#YYYY#-#MM#-#DD# ');
      $scope.fromDate = $scope.fromDate + $filter('date')($scope.myTimeFrom, 'HH:mm:ss');

      $scope.toDate = $scope.myDateTo.customFormat('#YYYY#-#MM#-#DD# ');
      $scope.toDate = $scope.toDate + $filter('date')($scope.myTimeTo, 'HH:mm:ss');

      $http({
        url: "getMeasurementsByDate",
        method: "GET",
        params: {from: $scope.fromDate, to: $scope.toDate}
      }).success(function (data) {
          $scope.data = data;
          $scope.done = true;
          $scope.tableParams = new NgTableParams(
            {
              page: 1,
              count: 10
            }, {
              total: $scope.data.length,
              getData: function ($defer, params) {
                // use build-in angular filter

                var filters = params.filter(),
                  exactFilters = {};
                if (filters.hasOwnProperty('txPower')) {
                  exactFilters.txPower = filters.txPower;
                  delete filters.txPower;
                }
                if (filters.hasOwnProperty('distance')) {
                  exactFilters.distance = filters.distance;
                  delete filters.distance;
                }
                var orderedData = params.filter() ?
                  $filter('filter')(data, filters) :
                  data;
                orderedData = exactFilters ?
                  $filter('filter')(orderedData, exactFilters, true) :
                  orderedData;
                orderedData = params.sorting() ?
                  $filter('orderBy')(orderedData, params.orderBy()) :
                  orderedData;
                filters.txPower = exactFilters.txPower;
                filters.distance = exactFilters.distance;

                $scope.data = orderedData;

                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
              }
            });
        })
        .error(function () {
          console.log("Błąd");
          $scope.done = true;
          $scope.showAlert("Brak komunikacji z serwerem...")
        });
      $scope.showDateRange = false;
    }
  };

  $scope.getAllData = function () {
    $scope.done = false;
    $http.get('/getMeasurements')
      .success(function (data) {
        $scope.data = data;
        $scope.done = true;
        $scope.tableParams = new NgTableParams({
          page: 1,            // show first page
          count: 10          // count per page
        }, {
          total: $scope.data.length, // length of data
          getData: function ($defer, params) {
            // use build-in angular filter

            var filters = params.filter(),
              exactFilters = {};
            if (filters.hasOwnProperty('txPower')) {
              exactFilters.txPower = filters.txPower;
              delete filters.txPower;
            }
            if (filters.hasOwnProperty('distance')) {
              exactFilters.distance = filters.distance;
              delete filters.distance;
            }
            var orderedData = params.filter() ?
              $filter('filter')(data, filters) :
              data;
            orderedData = exactFilters ?
              $filter('filter')(orderedData, exactFilters, true) :
              orderedData;
            orderedData = params.sorting() ?
              $filter('orderBy')(orderedData, params.orderBy()) :
              orderedData;
            filters.txPower = exactFilters.txPower;
            filters.distance = exactFilters.distance;

            $scope.data = orderedData;

            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          }
        });
      })
      .error(function () {
        console.log("Błąd");
        $scope.done = true;
        $scope.showAlert("Brak komunikacji z serwerem...")
      });
  };

  $scope.openFrom = function () {
    $scope.isFromOpened = true;
  };
  $scope.openTo = function () {
    $scope.isToOpened = true;
  };
  $scope.showAlert = function (text) {
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