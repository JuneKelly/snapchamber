'use strict'


angular.module('snapchamberApp')
  .controller 'MainCtrl', ($scope, $http, $location) ->

    $scope.reset = ->
      $scope.media = ""
      $scope.imageLink = ""
      $scope.imageSubmitted = false

    $scope.reset()

    $scope.saveMedia = ->
      $scope.imageSubmitted = true
      $http.post('/api/snap', {imageData: $scope.media})
        .success( (data, status, headers, config) ->
          console.log 'success'
          $scope.imageLink = window.location.href + 'view/' + data.snapId
        )
        .error( (data, status, headers, config) ->
          console.log 'failure'
          console.log data
        )

