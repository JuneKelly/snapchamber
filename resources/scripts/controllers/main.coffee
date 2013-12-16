'use strict'

angular.module('snaplyApp')
  .controller 'MainCtrl', ($scope, $http) ->
    $scope.media = ""
    $scope.imageLink = ""

    $scope.clearMedia = ->
      $scope.media = ""

    $scope.saveMedia = ->
      console.log 'SAVE MEDIA'
      $scope.imageLink = "http://example.com/view/asdfqwerzxcv"
