angular.module('snaplyApp')
  .factory 'CameraService', ($window) ->
    hasUserMedia = () ->
      return !!getUserMedia()


    getUserMedia = () ->
      navigator.getUserMedia = ($window.navigator.getUserMedia ||
                                $window.navigator.webkitGetUserMedia ||
                                $window.navigator.mozGetUserMedia ||
                                $window.navigator.msGetUserMedia)
      return navigator.getUserMedia

    hasUserMedia: hasUserMedia()
    getUserMedia: getUserMedia
