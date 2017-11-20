angular.module('tripPlannerApp', [])
    .config(function ($httpProvider) {
    })

    .controller('tripPlannerCtrl', function($scope, $http, $filter) {
        $scope.message = ""
        $scope.submitRequest = function (url) {
            $http({
                method : 'post',
                url : url,
                data : $scope.travelPlanRequest
            })
                .then(function (response) {
                    console.log(response.data)
                    $scope.tripPlan = response.data
                }),
                function errorCallBack (response) {
                    $scope.message = "Error in the server"
                    $scope.tripPlan = null
                }
        }
    })
