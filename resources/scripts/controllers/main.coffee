'use strict'

angular.module('snaplyApp')
  .controller 'MainCtrl', ($scope, $http, $location) ->
    $scope.media = ""
    $scope.imageLink = ""

    $scope.clearMedia = ->
      $scope.media = ""

    $scope.saveMedia = ->
      console.log 'SAVE MEDIA'
      $http.post('/api/snap', {imageData: $scope.media})
        .success( (data, status, headers, config) ->
          console.log 'success'
          $scope.imageLink = window.location.href + 'view/' + data.snapId
        )
        .error( (data, status, headers, config) ->
          console.log 'failure'
          console.log data
        )

      $scope.imageLink = "http://example.com/view/asdfqwerzxcv"
