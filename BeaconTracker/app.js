/**
 * Created by User on 2015-11-19.
 */

"use strict";

var BeaconTracker = angular.module('BeaconTracker', []);

BeaconTracker.controller('BeaconTrackerCtrl', function($scope, $http, $interval) {

  //var url = "http://78.88.254.200:8081/devices";
  var url = "http://192.168.0.9:8080/devices";

  var counter = 0;

  $scope.devices = [];

  $interval(function () {
    updateDevicesInRange()
  }, 1000);

  function updateDevicesInRange() {
    counter += 1;
    $http.get(url)
      .success(function (data) {
        console.log(counter + data);
          $scope.devices = data.devicesInRangeList;
          if($scope.devices.length === 0) {
            $scope.errorMessage = "Nie znaleziono żadnych urządzeń w pobliżu"
          } else {
            $scope.errorMessage = "";
          }
      })
      .error(function (data) {
        console.log(data);
          $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
      })
  }

});