/**
 * Created by User on 2015-11-30.
 */

var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('measurementsCtrl', function ($scope, $http, NgTableParams, $filter ) {

  $scope.done = false;

  $http.get('/getMeasurements')
    .success(function (data) {
      $scope.data = data;
      $scope.done = true;
      $scope.tableParams = new NgTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
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
          $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
      });
    })
    .error(function () {
      console.log("Błąd")
      $scope.done = true;
      showAlert("Brak komunikacji z serwerem...")
    });
  $scope.getDate = function(time) {
    var date = new Date(time);
    return date.customFormat( "#DD#/#MM#/#YYYY# #hh#:#mm#:#ss#" );
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