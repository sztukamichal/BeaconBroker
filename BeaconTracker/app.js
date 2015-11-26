/**
 * Created by User on 2015-11-19.
 */

"use strict";

var BeaconTracker = angular.module('BeaconTracker', ['ngMaterial','ngMdIcons']);

BeaconTracker.controller('BeaconTrackerCtrl', function($scope, $http, $interval, $mdDialog) {

  //var url = "http://78.88.254.200:8081/devices";
  var url = "http://192.168.0.9:8080/devices";

  var counter = 0;

  $scope.devices = [{"devicesInRangeList":[{"rssi":-78,"address":"EC:E7:42:7C:AB:2B"}],"deviceModel":"GT-I9515 - 1a88bbcf523c933c"}];
  $scope.view =[];
  $scope.isServerResponding = false;
  $scope.errorWithConnection = false;

  //fake
  $scope.isServerResponding = true;
  $scope.errorWithConnection = false;
  $scope.errorMessage ='';

  //$interval(function () {
  //  if($scope.errorWithConnection !== true) {
  //    updateDevicesInRange()
  //  }
  //}, 2000);

  function updateDevicesInRange() {
    counter += 1;
    $http.get(url)
      .success(function (data) {
        $scope.errorWithConnection = false;
        $scope.isServerResponding = true;
        console.log(data);
          $scope.devices = data;
          if($scope.devices.length === 0) {
            $scope.errorMessage = "Nie znaleziono żadnych urządzeń w pobliżu"
          } else {
            $scope.errorMessage = "";
          }
      })
      .error(function (data) {
        showAlert();
        $scope.isServerResponding = true;
        $scope.errorWithConnection = true;
        console.log(data);
          $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
      })
  }
  $scope.toggleView = function (index) {
    $scope.view[index] = !$scope.view[index];
  };

  var showAlert = function() {
    $mdDialog.show(
      $mdDialog.alert()
        .parent(angular.element(document.querySelector('#popupContainer')))
        .clickOutsideToClose(true)
        .title('Brak połączenia z serwerem')
        .ok('OK')
        .targetEvent(null)
    );
  };

});

