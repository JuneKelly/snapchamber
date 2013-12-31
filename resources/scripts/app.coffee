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
        templateUrl: 'static/views/main.html'
        controller: 'MainCtrl'

      .when '/about',
        templateUrl: 'static/views/about.html'
        controller: 'AboutCtrl'

      .when '/view/:snapId',
        templateUrl: 'static/views/snapview.html'
        controller: 'SnapViewCtrl'

      .when '/donate',
        templateUrl: 'static/views/donate.html'
        controller: 'DonateCtrl'

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
