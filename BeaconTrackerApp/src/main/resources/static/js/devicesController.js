/**
 * Created by User on 2015-11-27.
 */
var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('devicesCtrl', function($scope, $http, $interval, $mdDialog) {

  //var url = "http://78.88.254.200:8081/devices";
  var url = "/devices";
  var notTrackedInitialized = false;
  var time = 1000;
  $scope.devices = [];
  $scope.view =[];
  $scope.isServerResponding = false;
  $scope.errorWithConnection = false;
  $scope.distance = 0.0;
  $scope.beacon =  null;
  $scope.trackedDevices = [];
  $scope.notTrackedDevices = [];
  $scope.selectedIndex = 0;
  $scope.beacons =   [
    { id: 1, name: 'EC:E7:42:7C:AB:2B'},
    { id: 2, name: 'F9:39:03:69:9B:B4'}
  ];
  //fake
  //$scope.isServerResponding = true;
  //$scope.errorWithConnection = false;
  //$scope.errorMessage ='';
  //$scope.devices = [{"devicesInRangeList":[{"rssi":-78,"address":"EC:E7:42:7C:AB:2B"}],"deviceId":"GT-I9515 - 1a88bbcf523c933c"}];
  //$scope.notTrackedDevices = [
  //  {
  //    "deviceId":"GT-I9515 - 1a88bbcf523c933c",
  //    "distancesToBeacons": {}
  //}];
  //$scope.beacons =  $scope.beacons  || [
  //    { id: 1, name: 'EC:E7:42:7C:AB:2B'},
  //    { id: 2, name: 'F9:39:03:69:9B:B4'},
  //    { id: 3, name: 'EC:E7:42:7C:AB:22'},
  //    { id: 4, name: 'EC:E7:42:7C:AB:23'},
  //    { id: 5, name: 'EC:E7:42:7C:AB:24'}
  //  ];
  //$scope.trackedDevices = [
  //  {
  //    "deviceId":"GT-I9515 - 1a88bbcf523c933c",
  //    "distancesToBeacons": {
  //      "EC:E7:42:7C:AB:2B":1.2,
  //      "EC:E7:42:7C:AB:22":0.2,
  //      "EC:E7:42:7C:AB:23":2.2
  //    }
  //  }
  //];

  $interval(function () {
    if($scope.errorWithConnection !== true && $scope.selectedIndex === 0) {
      updateDevicesInRange()
    }
  },time);

  function initializeNotTrackedDevice(devices) {
    devices.forEach(function (element) {
      $scope.notTrackedDevices.push({"deviceId": element.deviceId, "distancesToBeacons": {}});
    })
  }

  $scope.toggleSelectedTab = function(index) {
    $scope.selectedIndex = index;
    if(index === 1) {
      time = 1000000;
      if(notTrackedInitialized === false) {
        notTrackedInitialized = true;
        var notTrackedDevices = [];
        var that = this;
        var devices = $scope.trackedDevices.length ===0 ? $scope.devices : $scope.devices.filter(function (element, index) {
          return $scope.trackedDevices.findIndex(function (trackedDevice) {
              if(trackedDevice.deviceId === element.deviceId) {
                return true;
              }
            }) > -1 ;
        });
        initializeNotTrackedDevice(devices);
      }
    } else {
      time = 1000;
    }
  };

  function updateDevicesInRange() {
    $http.get("/getDevicesInRange")
      .success(function (data) {
        $scope.errorWithConnection = false;
        $scope.isServerResponding = true;
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
        $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
      })
  }
  updateDevicesInRange();
  var getTrackedDevice = function () {
    $http.get('/getTrackedDevices')
      .success(function (data) {
        $scope.errorWithConnection = false;
        $scope.isServerResponding = true;
        $scope.trackedDevices = data;
      })
      .error(function (data) {
        showAlert();
        $scope.isServerResponding = true;
        $scope.errorWithConnection = true;
        console.log(data);
        $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
      })
  };

  getTrackedDevice();

  $scope.toggleView = function (index) {
    $scope.view[index] = !$scope.view[index];
  };
  $scope.getTrackedDevice = function (deviceId) {
    return $scope.trackedDevices.findIndex(function (element) {
      if(element.deviceId == deviceId) {
        return true;
      }
    });
  };
  $scope.removeProperty = function (index, key) {
    delete $scope.trackedDevices[index].distancesToBeacons[key];
  };
  $scope.changeProperty = function (index, key, value) {
    console.log(key);
    if(key != undefined) {
      $scope.trackedDevices[index].distancesToBeacons[key] = value;
    }
  };
  $scope.getBeacons = function (index) {
    return $scope.beacons.filter(function (element) {
      return !$scope.trackedDevices[index].distancesToBeacons.hasOwnProperty(element.name);
    })
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

