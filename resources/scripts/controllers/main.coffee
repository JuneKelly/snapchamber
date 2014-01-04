'use strict'


angular.module('snapchamberApp')
  .controller 'MainCtrl', ($scope, $http, $location) ->

    isSupported = () ->
      if (navigator.getUserMedia) or
      (navigator.webkitGetUserMedia) or
      (navigator.mozGetUserMedia) or
      (navigator.msGetUserMedia) then true
      else false

    $scope.reset = () ->
      $scope.media = ""
      $scope.imageLink = ""
      $scope.imageSubmitted = false
      $scope.saveButtonText = 'Save'
      $scope.isSupported = isSupported()

    $scope.reset()

    $scope.saveMedia = ->
      $scope.imageSubmitted = true
      $scope.saveButtonText = 'Please Wait'

      $http.post('/api/snap', {imageData: $scope.media})
        .success( (data, status, headers, config) ->
          console.log 'success'
          $scope.imageLink = window.location.href + 'view/' + data.snapId
          $scope.saveButtonText = 'Save'
        )
        .error( (data, status, headers, config) ->
          console.log 'failure'
          console.log data
          $scope.saveButtonText = 'Save'
        )

