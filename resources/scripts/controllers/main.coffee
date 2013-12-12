'use strict'

angular.module('snaplyApp')
  .controller 'MainCtrl', ($scope, $http) ->
    $scope.image = null
    $scope.niceThings = []

    loadNiceThings = () ->
      $http(
        method: 'GET'
        url: '/api/nicethings'
        headers: { 'Accept': 'application/json'}
      )
        .success (data) ->
          $scope.niceThings = data.things

    loadNiceThings()
