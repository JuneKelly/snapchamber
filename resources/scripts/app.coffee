'use strict'

angular.module('snaplyApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config ($routeProvider, $sceDelegateProvider) ->
    $sceDelegateProvider.resourceUrlWhitelist ['self', 'blob:**']

    $routeProvider
      .when '/',
        templateUrl: 'views/main.html'
        controller: 'MainCtrl'
      .otherwise
        redirectTo: '/'
