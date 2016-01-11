/**
 * Created by User on 2015-11-19.
 */

"use strict";

var BeaconTracker = angular.module('BeaconTracker', [
  'ngMaterial',
  'ui.bootstrap',
  'ngMdIcons',
  'ngRoute',
  'ngTable',
  'ngTableExport',
  'chart.js',
  'colorpicker.module'
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
    when('/configuration', {
        templateUrl : 'html/configuration.html',
        controller: 'configurationCtrl'
      }).
      when('/localization', {
          templateUrl : 'html/localization.html',
          controller: 'localizationCtrl'
        }).
    otherwise({
      redirectTo: '/measurements'
    });

  }
])

BeaconTracker.config(function($mdDateLocaleProvider) {
    $mdDateLocaleProvider.formatDate = function(date) {
    	console.log(date);
        return moment(date).format('YYYY-MM-DD');
     };
 });

;
