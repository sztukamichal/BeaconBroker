/**
 * Created by User on 2015-11-19.
 */

"use strict";

var BeaconTracker = angular.module('BeaconTracker', [
  'ngMaterial',
  'ngMdIcons',
  'ngRoute',
  'ngTable'
]);

BeaconTracker.config(['$routeProvider',
  function ($routeProvider) {

    $routeProvider.when('/devices', {
      templateUrl : 'html/devices.html',
      controller: 'devicesCtrl'
    }).
    when('/measurements', {
      templateUrl : 'html/measurements.html',
      controller: 'measurementsCtrl as vm'
    }).
    otherwise({
      redirectTo: '/home'
    });

  }
]);
