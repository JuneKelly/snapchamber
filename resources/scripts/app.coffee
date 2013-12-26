'use strict'

angular.module('snapchamberApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'omr.directives'
])
  .config ($routeProvider, $sceDelegateProvider) ->
    $sceDelegateProvider.resourceUrlWhitelist ['self', 'blob:**']

    $routeProvider
      .when '/',
        templateUrl: 'views/main.html'
        controller: 'MainCtrl'
      .when '/about',
        templateUrl: 'views/about.html'
        controller: 'AboutCtrl'
      .when '/view/:snapId',
        templateUrl: 'views/snapview.html'
        controller: 'SnapViewCtrl'
      .otherwise
        redirectTo: '/'


# collapse the menu on click
$(->
  navMain = $("#main-menu")

  navMain.on(
    "click",
    "a",
    null,
    () -> navMain.collapse('hide')
  )
)
