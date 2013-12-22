'use strict'

angular.module('snaplyApp')
  .controller 'SnapViewCtrl', ($scope, $http, $routeParams) ->
    $scope.snapId = $routeParams.snapId
    $scope.media = ""

    getMedia = ->
      $http.get('/api/snap/'+$scope.snapId)

        .success (data, status, headers, config) ->
          console.log 'success load'
          console.log data
          $scope.media = data.imageData

        .error (data, status, headers, config) ->
          console.log 'ERROR'
          console.log data

    getMedia()
