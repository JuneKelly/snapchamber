'use strict'

angular.module('snaplyApp')
  .controller 'MainCtrl', ($scope, $http) ->
    $scope.media = ""

    $scope.clearMedia = ->
      $scope.media = ""

    $scope.saveMedia = ->
      console.log 'SAVE MEDIA'
