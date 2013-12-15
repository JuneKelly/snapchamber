angular.module('snaplyApp')
  .directive 'camera', (CameraService) ->
    restrict: 'EA',
    replace: true,
    transclude: true,
    scope: {},
    controller: ($scope, $q, $timeout) ->
      this.takeSnapshot = () ->
        canvas  = document.querySelector('canvas')
        ctx     = canvas.getContext('2d')
        videoElement = document.querySelector('video')
        d       = $q.defer()

        canvas.width = $scope.w
        canvas.height = $scope.h

        $timeout( ->
          ctx.fillRect(0, 0, $scope.w, $scope.h)
          ctx.drawImage(videoElement, 0, 0, $scope.w, $scope.h)
          d.resolve(canvas.toDataURL())
        , 0)
        d.promise
      return this
    template: '<div class="camera"><video class="camera" autoplay="" /><div ng-transclude></div></div>',
    link: (scope, ele, attrs) ->
      w = attrs.width || 320
      h = attrs.height || 200

      if (!CameraService.hasUserMedia)
        return
      else
        userMedia = CameraService.getUserMedia()
        videoElement = document.querySelector('video')


        onSuccess = (stream) ->
          if (navigator.mozGetUserMedia)
            videoElement.mozSrcObject = stream
          else
            vendorURL = window.URL || window.webkitURL
            videoElement.src = window.URL.createObjectURL(stream)
          # Just to make sure it autoplays
          videoElement.play()

        # If there is an error
        onFailure = (err) ->
          console.error(err)

        # Make the request for the media
        navigator.getUserMedia({
          video: {
            mandatory: {
              maxHeight: h
              maxWidth: w
            }
          },
          audio: false
        }, onSuccess, onFailure)

        scope.w = w
        scope.h = h

