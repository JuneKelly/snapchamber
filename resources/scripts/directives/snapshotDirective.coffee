angular.module('snaplyApp')
  .directive 'cameraControlSnapshot', () ->
    restrict: 'EA',
    require: '^camera',
    scope: true,
    template: '<a class="btn btn-info" ng-click="takeSnapshot()">Take snapshot</a>',
    link: (scope, ele, attrs, cameraCtrl) ->
      scope.takeSnapshot = () ->
        cameraCtrl.takeSnapshot()
          .then (image) ->
            console.log image
            scope.$parent.$parent.media = image
