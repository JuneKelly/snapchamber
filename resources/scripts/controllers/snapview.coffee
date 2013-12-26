'use strict'

angular.module('snapchamberApp')
  .controller 'SnapViewCtrl', ($scope, $http, $routeParams) ->
    $scope.snapId = $routeParams.snapId
    $scope.media = ""

    getMedia = ->
      $http.get('/api/snap/'+$scope.snapId)

        .success (data, status, headers, config) ->
          $scope.media = data.imageData

        .error (data, status, headers, config) ->
          console.log data

    getMedia()
