angular.module('tripPlannerApp', [])
    .config(function ($httpProvider) {
    })

    .controller('tripPlannerCtrl', function($scope, $http, $filter) {
        $scope.message = ""
        $scope.submitRequest = function (url) {
            $http({
                method : 'post',
                url : url,
                data : $scope.travelPlan
            })
                .then(function (response) {
                    console.log(response.data)
                    $scope.message = response.data.message;
                }),
                function errorCallBack (response) {
                    $scope.message = "Error in the server"
                }
        }
    })
