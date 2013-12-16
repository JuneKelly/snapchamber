'use strict'

angular.module('snaplyApp')
  .controller 'MainCtrl', ($scope, $http, CameraService) ->
    $scope.media = ""
    $scope.hasUserMedia = CameraService.hasUserMedia


    $scope.clearMedia = ->
      $scope.media = ""
