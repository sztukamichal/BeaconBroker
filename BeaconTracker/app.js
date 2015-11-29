/**
 * Created by User on 2015-11-19.
 */

"use strict";

var BeaconTracker = angular.module('BeaconTracker', [
  'ngMaterial',
  'ngMdIcons',
  'ngRoute'
]);

BeaconTracker.config(['$routeProvider',
  function ($routeProvider) {

    $routeProvider.when('/devices', {
      templateUrl : 'html/devices.html',
      controller: 'devicesCtrl'
    }).
    otherwise({
      redirectTo: '/home'
    });

  }
]);
