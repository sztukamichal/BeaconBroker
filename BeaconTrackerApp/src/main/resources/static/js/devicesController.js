/**
 * Created by User on 2015-11-27.
 */
var BeaconTracker = angular.module('BeaconTracker');

BeaconTracker.controller('devicesCtrl', function($scope, $http, $interval, $mdDialog, $timeout) {

  //var url = "http://78.88.254.200:8081/devices";
  var url = "/devices";
  $scope.devices = [];
  $scope.view =[];
  $scope.isServerResponding = false;
  $scope.errorWithConnection = false;
  $scope.distance = 0.0;
  $scope.beacon =  null;
  $scope.trackedDevices = [];
  $scope.devicesConfigurations = [];
  $scope.selectedIndex = 0;
  $scope.beacons =   [
    { id: 1, name: 'EC:E7:42:7C:AB:2B'},
    { id: 2, name: 'F9:39:03:69:9B:B4'},
    { id: 3, name: 'F4:1C:50:21:23:FD'},
    { id: 4, name: 'D4:E0:02:F8:70:3C'},
    { id: 5, name: 'C8:F1:74:81:5E:D3'}
  ];
  //fake
  //$scope.isServerResponding = true;
  //$scope.errorWithConnection = false;
  //$scope.errorMessage ='';
  //$scope.devices = [{"devicesInRangeList":[{"rssi":-78,"address":"EC:E7:42:7C:AB:2B"}],"deviceId":"GT-I9515 - 1a88bbcf523c933c"}];
  //$scope.devicesConfigurations = [
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
  },1000);

  function addDevicesConfiguration(devices) {
    devices.forEach(function (element) {
      $scope.devicesConfigurations.push({"deviceId": element.deviceId, "distancesToBeacons": {}});
    })
  }

  $scope.toggleSelectedTab = function(index) {
    $scope.selectedIndex = index;
  };

  function updateConfigurations(newDevices) {
    addDevicesConfiguration(newDevices);
    $scope.trackedDevices.forEach(function (configuration) {
      var foundIndex = newDevices.findIndex(function (element) {
        return element.deviceId === configuration.deviceId;
      });
      if (foundIndex > -1) {
        $scope.devicesConfigurations[foundIndex].distancesToBeacons = configuration.distancesToBeacons;
      }
    });
  }

  function updateDevicesInRange() {
    $http.get("/getDevicesInRange")
      .success(function (data) {
        $scope.errorWithConnection = false;
        $scope.isServerResponding = true;
        if($scope.devices < data.length) {
          var newDevices = data.filter(function (element) {
            return $scope.devices.findIndex(function (el) {
                return element.deviceId == el.deviceId;
              }) < 0;
          });
          updateConfigurations(newDevices);
        }
        $scope.devices = data;
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

  function getTrackedDevice() {
    $http.get('/getTrackedDevices')
      .success(function (data) {
        $scope.errorWithConnection = false;
        $scope.isServerResponding = true;
        $scope.trackedDevices = data;
        console.log($scope.devices);
        console.log($scope.trackedDevices);
        console.log($scope.devicesConfigurations);
      })
      .error(function () {
        showAlert("Brak połączenia z serwerem");
        $scope.isServerResponding = true;
        $scope.errorWithConnection = true;
        $scope.trackedDevices = [];
        $scope.errorMessage = "Błąd podczas komunikacji z serwerem...";
      })
  }
  getTrackedDevice()

  $scope.toggleView = function (index) {
    $scope.view[index] = !$scope.view[index];
  };

  $scope.startTrackDevice = function (trackDevice) {
    $http({
      method: 'POST',
      url: '/trackDevice',
      data: trackDevice,
      headers: {'Content-Type': 'application/json'}
    })
      .success(function (data) {
        $scope.trackedDevices.push(trackDevice);
        console.log("success");
      })
      .error(function () {
        showAlert("Brak połączenia z serwerem");
      })
  };
  $scope.stopTrackDevice = function (trackDevice, index) {
    $http({
      method: 'POST',
      url: '/stopTrackDevice',
      data: trackDevice,
      headers: {'Content-Type': 'application/json'}
    })
      .success(function (data) {
        $scope.trackedDevices.splice(index,1);
        console.log("success");
      })
      .error(function () {
        showAlert("Brak połączenia z serwerem");
      })
  }

  $scope.removeProperty = function (index, key) {
    delete $scope.devicesConfigurations[index].distancesToBeacons[key];
  };

  $scope.changeProperty = function (index, key, value) {
    if(key != undefined) {
      $scope.devicesConfigurations[index].distancesToBeacons[key] = value;
    }
  };

  $scope.getAvailableBeacons = function (device, index) {
    //var availableBeacons = [];
    //var counter = 0;
    //device.beaconsInRangeList.forEach(function (element) {
    //  if( !$scope.devicesConfigurations[index].distancesToBeacons.hasOwnProperty(element.address)) {
    //    availableBeacons.push(
    //      { id: counter, name: element.address});
    //    counter += 1;
    //  }
    //});
    //return availableBeacons;
    return $scope.beacons.filter(function (element) {
      return !$scope.devicesConfigurations[index].distancesToBeacons.hasOwnProperty(element.name);
    })
  };

  $scope.getDeviceConfigurationIndex = function (deviceId) {
    return $scope.devicesConfigurations.findIndex(function (element) {
      if(element.deviceId == deviceId) {
        return true;
      }
    });
  };

  $scope.getTrackedDeviceIndex = function (deviceId) {
    return $scope.trackedDevices.findIndex(function (element) {
      if(element.deviceId == deviceId) {
        return true;
      }
    });
  };

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

