'use strict'

angular.module('snapchamberApp')
  .controller 'SnapViewCtrl', ($scope, $http, $routeParams) ->
    $scope.snapId = $routeParams.snapId
    $scope.media = ""
    $scope.errorMessage = ""
    $scope.imageLink = window.location.href

    getMedia = ->
      $http.get('/api/snap/'+$scope.snapId)

        .success (data, status, headers, config) ->
          $scope.media = data.imageData

        .error (data, status, headers, config) ->
          console.log data
          console.log status
          if status = 404
            $scope.errorMessage = "Snap not found."
          else
            $scope.errorMessage = "Something went horribly wrong"

    getMedia()
