/**
 * Created by saif-dream on 11/27/2015.
 */
(function(angular) {
    'use strict';

    var edit = angular.module('edit', ['ngAnimate','xeditable','ui.bootstrap','elif','angularjs-dropdown-multiselect','ngComboDatePicker','ngMessages','ngMessageFormat']);
    edit.run(function(editableOptions, editableThemes) {
        editableThemes.bs3.inputClass = 'input-sm';
        editableThemes.bs3.buttonsClass = 'btn-sm';
        editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
    });

    edit.controller('editController', function ($scope, $filter, $injector, $parse, $log) {

        $scope.user = {};

        $scope.gender = [
            {value: 1, text: 'Male'},
            {value: 2, text: 'Female'}
        ];
        $scope.showGender = function() {
            var selected = $filter('filter')($scope.gender, {value: $scope.user.gender});
            return ($scope.user.gender && selected.length) ? selected[0].text : 'Empty';
        };

        $scope.isMarried = [
            {value: 1, text: 'Married'},
            {value: 2, text: 'Unmarried'}
        ];
        $scope.showMaritualStatus = function() {
            var selected = $filter('filter')($scope.isMarried, {value: $scope.user.isMarried});
            return ($scope.user.isMarried && selected.length) ? selected[0].text : 'Empty';
        };

        $scope.cities = [
            {value: 1, text: "Dhaka"},
            {value: 2, text: "Barisal"},
            {value: 3, text: "Comilla"},
            {value: 4, text: "CTG"},
            {value: 5, text: "Rajshahi"},
            {value: 6, text: "Chandpur"},
            {value: 7, text: "Khulna"},
            {value: 8, text: "Dinajpur"},
            {value: 9, text: "Rangpur"},
            {value: 10, text: "Sylhet"}
        ];
        $scope.showCity = function() {
            var selected = $filter('filter')($scope.cities, {value: $scope.user.city});
            return ($scope.user.city && selected.length) ? selected[0].text : 'Empty';
        };


        $scope.stringProcessor = function(){
            var result = "";
            $scope.arrString = [];

            var givenString = "Long {1} text{2}, Abul,1,12 Dec"; //"{1} {2} {3} {4} {5} {6} {7} {8} {9} {10} , 1,2,3,4,5,6,7,8,9,10";
            $log.log("Given String : " + givenString);

            var  resultString  = givenString.split(',')[0];
            $log.log("Result String : " + resultString);

            var totalCount = givenString.split(',').length-1;
            var secCount = resultString.split('{').length-1;

            if (totalCount <= secCount) {
                $log.log("There is not enough variable!");
                return;
            }

            for(var i=1; i<=totalCount; i++){
                $scope.arrString.push(givenString.split(',')[i]);
                $log.log("arrString : " + $scope.arrString);
            };

            for(var i=1; i<=secCount; i++){
                var replaceString = "{"+i+"}";
                result = resultString.replace(replaceString, $scope.arrString[i-1]);
                resultString = result;
                $log.log("Result : " + result);
            }
        };
        //$scope.stringProcessor();

        $scope.unlimitedArgumentsProcessor = function(/**/){
            var args = Array.prototype.slice.call(arguments);
            var result = "";
            //var arrString = [];
            /*
            for(var i=1; i<args.length; i++){
                arrString.push(args[i]);
            };
            */
            //$log.log("Given Arguments: " + args);

            var  resultString  = args[0];
            //$log.log("Result String: " + resultString);

            var stringLength = resultString.split('{').length-1;
            var variableLength = args.length-1;

            if (args.length-1 < resultString.split('{').length-1) {
                //$log.log("There is not enough variable! ("+stringLength+","+variableLength+")");
                return;
            }

            for(var i=1; i<=args.length-1; i++){
                var replaceString = "{"+i+"}";
                result = resultString.replace(replaceString,args[i]);
                resultString = result;
                //$log.log("Result: " + result);
            }
        };
        //$scope.unlimitedArgumentsProcessor("Long {1} {3} text{2}","Abul, Babul",1,"12 Dec");
        //$scope.unlimitedArgumentsProcessor("%{1}%", "Abul");
        //$scope.unlimitedArgumentsProcessor("Long {1} {3} text{2}","Abul, Babul","12 Dec");

        function massageProcessor(/**/){
            var args = Array.prototype.slice.call(arguments);
            //var result = "";
            if(args.length < 1) return "";
            console.log("Given Arguments: " + args);

            for(var i=1; i<=args.length-1; i++){
                args[0] = args[0].split("{"+i+"}").join(arguments[i]);
                //args[0] = args[0].replace(replaceString,args[i]);
            };
            console.log("Result: " + args[0]);
        };
        massageProcessor("Long {1} {3} text{2}, {2}","Abul, Babul",1,"12 Dec");
        //massageProcessor("%{1}%", "Abul");
        //massageProcessor("Long {1} {3} text{2}","Abul, Babul","12 Dec");

        function message() {
            var len = arguments.length;
            if(len < 1)
                return "";

            var msg = arguments[0];

            for(var i=1; i < len; i++) {
                msg = msg.split("{"+i+"}").join(arguments[i]);  // replace all
            }

            return msg;
        }

        console.log(message("{1} and {2} are two brothers, {2} is older than {1}.", "Abul", "Babul"));
        console.log(message("{1} and {2} are two brothers, {2} is older than {1}.", "Abul", "Babul", "Kabul"));
        console.log(message("{1} and {2} are two brothers, {2} is older than {1}.", "Abul"));

    });

})(window.angular);