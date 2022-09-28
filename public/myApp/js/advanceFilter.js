/**
 * Created by saif-dream on 10/22/2015.
 */
(function(angular) {
    'use strict';

    var advanceList = angular.module('advanceList',
        [
            'ngAnimate',
            'ui.bootstrap',
            'gm.datepickerMultiSelect',
            'ngTagsInput',
            'elif',
            'angularjs-dropdown-multiselect',
            'ngComboDatePicker',
            'bw.paging',
            'nya.bootstrap.select'
        ]);

    advanceList.controller('AdvanceFilterController', function ($scope, $filter, $compile, $http, advanceListService, $log, $q, $timeout) {
        //$scope.listData = advanceListService.getList();

        /* Basic Form Processing */
        $scope.formAdvance = {};

        /* Wizard Form Processing */
        $scope.wizadForm = {};

        $scope.listData = null;

        $scope.StringFilterList = [
            {id: 1, label: 'Equals'},
            {id: 2, label: 'Not Equals'},
            {id: 3, label: 'Start with'},
            {id: 4, label: 'Not Start with'},
            {id: 5, label: 'End with'},
            {id: 6, label: 'Not End with'},
            {id: 7, label: 'Contains'},
            {id: 8, label: 'Not Contains'},
            {id: 15, label: 'In'},
            {id: 16, label: 'Not In'},
            {id: 17, label: 'Null'},
            {id: 18, label: 'Not Null'},
            {id: 19, label: 'Sounds Like'}
        ];
        $scope.NumberFilterList = [
            {id: 1, label: 'Equals'},
            {id: 2, label: 'Not Equals'},
            {id: 9, label: 'Less than'},
            {id: 10, label: 'Less than eq'},
            {id: 11, label: 'Getter than'},
            {id: 12, label: 'Getter than eq'},
            {id: 13, label: 'Between'},
            {id: 14, label: 'Not Between'},
            {id: 15, label: 'In'},
            {id: 16, label: 'Not In'},
            {id: 17, label: 'Null'},
            {id: 18, label: 'Not Null'}
        ];
        $scope.DateFilterList = [
            {id: 1, label: 'Equals'},
            {id: 2, label: 'Not Equals'},
            {id: 9, label: 'Less than'},
            {id: 10, label: 'Less than eq'},
            {id: 11, label: 'Getter than'},
            {id: 12, label: 'Getter than eq'},
            {id: 13, label: 'Between'},
            {id: 14, label: 'Not Between'},
            {id: 15, label: 'In'},
            {id: 16, label: 'Not In'},
            {id: 17, label: 'Null'},
            {id: 18, label: 'Not Null'},
            {id: 20, label: 'Month Like'}
        ];

        $scope.genderList = [
            {value: "M", label: 'Male'},
            {value: "F", label: 'Female'}
        ];
        $scope.maritalStatus = [
            {value: true, label: 'Married'},
            {value: false, label: 'Unmarried'}
        ];

        /* Multi dropdown */
        $scope.formAdvance.cityBasic = [];
        $scope.cityAdvanec = [];
        $scope.city = angular.fromJson($scope.cityBasic);

        /*$scope.cityData = [
            {id: 1, label: "Dhaka"},
            {id: 2, label: "Barisal"},
            {id: 3, label: "Comilla"},
            {id: 4, label: "CTG"},
            {id: 5, label: "Rajshahi"},
            {id: 6, label: "Chandpur"},
            {id: 7, label: "Khulna"},
            {id: 8, label: "Dinajpur"},
            {id: 9, label: "Rangpur"},
            {id: 10, label: "Sylhet"}
        ];*/

        /* CSS Label Work */
        $scope.$watch('sortColumnName', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            //$log.log("sortColumnName: "+newValue);
            document.getElementById(oldValue+"Col").classList.remove('selectedcolumn');
            document.getElementById(oldValue).className = "sort sort-btn";
            document.getElementById(newValue+"Col").className = "selectedcolumn";
            document.getElementById(newValue).classList.remove('sort-btn');
        }, true);

        /* Column Sorting Function */
        $scope.setColumnValue = function(name){

            if($scope.sortColumnName == name){
                //document.getElementById("ID").className = "selectedcolumn";
                if($scope.isAscending == true){
                    $scope.isAscending = false;

                } else {
                    $scope.isAscending = true;
                }
            } else {
                $scope.isAscending = true;
            }
            $scope.sortColumnName = name;
            $scope.getFormData();
        }

        $scope.firstRun = function(col){
            var colId = col+"Col";
            $scope.sortColumnName = col;
            $scope.isAscending = true;
            document.getElementById(colId).className = "selectedcolumn";
            document.getElementById(col).classList.remove('sort-btn');
        }
        $scope.firstRun("id");

        /*$scope.clear = function(){
            var str = "No Filter"
            $scope.idFilter = str;
            $scope.usernameFilter = str;
            $scope.fullNameFilter =str;
            $scope.cityFilter = str;
            $scope.dobFilter = str;
            $scope.salaryFilter = str;
            //$scope.cityBasic = [];
            //$log.log( "I am in clear value is : " + $scope.fullNameFilter );
            $scope.filter = false;
        };
        $scope.clear();*/

        /*$scope.setFilter = function(scopeName, filterType) {
            if(scopeName == "idFilter"){
                $scope.idFilter = filterType;

            } else if(scopeName == "usernameFilter"){
                $scope.usernameFilter = filterType;

            } else if(scopeName == "fullNameFilter"){
                $scope.fullNameFilter = filterType;

            } else if(scopeName == "cityFilter"){
                $scope.cityFilter = filterType;

            } else if(scopeName == "dobFilter"){
                $scope.dobFilter = filterType;

            } else if(scopeName == "salaryFilter"){
                $scope.salaryFilter = filterType;

            } else {}

            $log.log( "I am in setCaretToPos value is : " + $scope.fullNameFilter );
            $scope.filter = true;
        };*/

        /*$scope.resetFormWizard = function(){
            $('#formWizard_id').val('No Filter');
            $('#formWizard_dob').val('No Filter');
            $('#formWizard_salary').val('No Filter');
            $('#formWizard_username').val('No Filter');
            $('#formWizard_fullName').val('No Filter');
            $('#formWizard_city').val('No Filter');
            $('#formWizard_cityModel').val('No Filter');
            $('#formWizard_id').selectpicker('refresh');
            $('#formWizard_dob').selectpicker('refresh');
            $('#formWizard_city').selectpicker('refresh');
            $('#formWizard_salary').selectpicker('refresh');
            $('#formWizard_username').selectpicker('refresh');
            $('#formWizard_fullName').selectpicker('refresh');
            $('#formWizard_city').selectpicker('refresh');
            $('#formWizard_cityModel').selectpicker('refresh');
        };

        $scope.resetformAdvance = function(){
            $('#formAdvance_gender').val('');
            $('#formAdvance_isMarried').val('');
            $('#formAdvance_city').val('');
            $('#formAdvance_gender').selectpicker('refresh');
            $('#formAdvance_isMarried').selectpicker('refresh');
            $('#formAdvance_city').selectpicker('refresh');
        };*/

        $scope.wizadForm.id = [];
        $scope.wizadForm.dob = [];
        $scope.wizadForm.salary = [];
        $scope.wizadForm.username = [];
        $scope.wizadForm.fullName = [];
        $scope.wizadForm.gender = [];
        $scope.wizadForm.isMarried = [];

        $scope.formAdvance.id = [];
        $scope.formAdvance.dob = [];
        $scope.formAdvance.salary = [];
        $scope.formAdvance.username = [];
        $scope.formAdvance.fullName = [];
        $scope.formAdvance.gender = [];
        $scope.formAdvance.isMarried = [];

        // I apply the remote data to the local scope.
        var filteredData = {};
        function applyRemoteData( listData ) {
            filteredData = listData;
            $scope.listData = filteredData.data;

            $scope.currentPage = filteredData.currentPage;
            $scope.rowsPerPage = filteredData.rowsPerPage;
            $scope.totalPageCount = filteredData.totalPageCount;
            $scope.totalRowsCount = filteredData.totalRowsCount;
            $scope.displayCounter = filteredData.displayCounter;

            $scope.sortColumnName = filteredData.sortColumnName;
            $scope.isAscending = filteredData.isAscending;
        }
        // I load the remote data from the server.
        function loadListData() {
            // The advanceListService returns a promise.
            advanceListService.getList().then(
                function( listData ) {
                    applyRemoteData( listData );
                }
            );
        }

        var filterTypeIds = {};
        var jsonData = {};

        // I hold the handle on the current request for data. Since we want to
        // be able to abort the request, mid-stream, we need to hold onto the
        // request which will have the .abort() method on it.
        var requestForData = null;
        // ---
        // PUBLIC METHODS.
        // ---
        // I abort the current request (if its running).
        $scope.abortRequest = function(){ return( requestForData && requestForData.abort() ); };

        $scope.isLoading = false;
        $scope.jsonData = function(data){
            filterTypeIds = {
                //"id":data.idFilter,
                "username":data.usernameFilter,
                "fullName":data.fullNameFilter,
                "dob":data.dobFilter,
                "gender":data.genderFilter,
                "isMarried":data.isMarriedFilter,
                "salary":data.salaryFilter,
                "city":data.cityFilter
            };

            var y;
            for (var x in filterTypeIds) {
                if ( Object.prototype.hasOwnProperty.call(filterTypeIds,x)) {
                    y = filterTypeIds[x];
                    if (y==="null" || y===null || y==="" || typeof y === "undefined") {
                        delete filterTypeIds[x];
                    }
                }
            }

            jsonData = {
                //"id":data.id,
                "username":data.username,
                "fullName":data.fullName,
                "dob":data.dob,
                "gender":data.gender,
                "isMarried":data.isMarried,
                "salary":data.salary,
                "city":data.city,
                "filterTypeId":filterTypeIds,
                "currentPage":$scope.currentPage,
                "rowsPerPage":$scope.rowsPerPage,
                "sortColumnName": $scope.sortColumnName,
                "isAscending":$scope.isAscending
            };

            $scope.isLoading = true;
            $scope.errorMassage = "";
            $scope.abortMassage = "";

            $scope.listData = null;

            ( requestForData = advanceListService.listRequest( "/test/filter/json", jsonData ) ).then(
            //advanceListService.listRequest( jsonData ).then(
                //$scope.loadRemoteData(),
                function( listData ) {
                    if(!listData){
                        $log.warn("No Data have Seen!");
                    }else{
                        applyRemoteData(listData);
                        $scope.isLoading = false;
                    }
                },
                function( errorMessage ) {
                    $log.warn( errorMessage );
                    if(angular.equals(errorMessage,"Request Aborted")){
                        $scope.abortMassage = "Request Aborted By User.";

                        $scope.currentPage = 1;
                        $scope.totalPageCount = 0;
                        $scope.totalRowsCount = 0;
                        $scope.displayCounter = "0 to 0 from 0";
                        //$scope.sortColumnName = filteredData.sortColumnName;
                        //$scope.isAscending = filteredData.isAscending;
                    } else {
                        $scope.errorMassage = errorMessage;
                    }
                    $scope.isLoading = false;
                }
            );
        }

        /*Genetare Page Number*/
        $scope.pageNumber = [1];
        $scope.$watch('totalPageCount', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            $scope.pageNumber = [];
            for (var i = 1; i <= newValue; i++) {
                $scope.pageNumber.push(i);
            }
        }, true);

        $scope.maxSize = 5;
        $scope.doCtrlPagingAct = function(currentPage){
            $scope.currentPage = currentPage;
            $scope.jsonData($scope.wizadForm);
        }

        $scope.formName = "";
        $scope.getData = function(formName,form){
            $scope.currentPage = 1;
            $scope.formName = formName;
            $scope.jsonData(form);
            //$log.log("Form Name: "+$scope.formName+", "+$scope.currentPage+" Submitted!" );
        }

        $scope.getFormData = function(){
            if($scope.formName === "wizadForm"){
                $scope.jsonData($scope.wizadForm);
                //$log.log("Get Rows for wizadForm.");
            }

            if($scope.formName === "formAdvance"){
                $scope.jsonData($scope.formAdvance);
                //$log.log("Get Rows for formAdvance.");
            }

            if($scope.formName === ""){
                $scope.jsonData($scope.formAdvance);
                //$log.log("Primary Form is formAdvance.");
            }
        }

        $scope.getData('formAdvance',$scope.formAdvance);

        // I process the list form.
        /*$scope.getformAdvanceList = function() {
            // If the data we provide is invalid, the promise will be rejected
            //$log.log($scope.formAdvance);
            advanceListService.listRequest( $scope.formAdvance ).then(
                //$scope.loadRemoteData(),
                function( listData ) {
                    applyRemoteData( listData );
                },
                function( errorMessage ) {
                    $log.warn( errorMessage );
                }
            );
        };*/

        // I process the list form.
        /*$scope.getFormAdvanceList = function(jsonData) {
            // If the data we provide is invalid, the promise will be rejected
            $log.log(jsonData);
            advanceListService.listRequest( jsonData ).then(
                //$scope.loadRemoteData(),
                function( listData ) {
                    if(!listData){
                        $log.warn("No Data have Seen!");
                    }else{
                        applyRemoteData(listData);
                    }
                },
                function( errorMessage ) {
                    $log.warn( errorMessage );
                }
            );
        };*/

        /* Accordion */
        $scope.oneAtATime = true;
        $scope.isOpen = true;

        $scope.status = {
            openAccordion: true,
            isFirstOpen: true
        };

        /*Date Properties*/
        $scope.maxDate = new Date(2050, 5, 22);

        $scope.toggleMin = function() {
            $scope.minDate = $scope.minDate ? null : new Date();
        };

        $scope.calendar = {
            opened: {},
            dateFormat: 'y/MM/dd',
            dateOptions: {
                formatYear: 'y',
                startingDay: 1
            },
            open: function($event, which) {
                $scope.toggleMin();
                //$event.preventDefault();
                //$event.stopPropagation();
                $scope.calendar.opened[which] = true;
            }
        };

        /*$scope.openPicker = function() {
            $scope.opened = true;
            console.log($scope.opened);
        };

        // Disable weekend selection
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.date = function($event) {
            $scope.date.open = true;
        };

        $scope.dateFrom = function($event) {
            $scope.dateFrom.openFrom = true;
        };

        $scope.dateTo = function($event) {
            $scope.toggleMin();
            $scope.dateTo.opendateTo = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['shortDate','y/MM/dd','dd-MMMM-yyyy','yyyy/MM/dd','dd.MM.yyyy'];
        $scope.format = $scope.formats[1];

        $scope.status = {
            opened: false
        };*/

        var tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);

        var afterTomorrow = new Date();
        afterTomorrow.setDate(tomorrow.getDate() + 2);
        $scope.events =
            [
                {
                    date: tomorrow,
                    status: 'full'
                },
                {
                    date: afterTomorrow,
                    status: 'partially'
                }
            ];

        /*$scope.getDayClass = function(date, mode) {
            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0,0,0,0);

                for (var i=0;i<$scope.events.length;i++){
                    var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }
            return '';
        };*/

        /*$scope.$watch('wizadForm.dob', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            $scope.wizadForm.dob = $filter('date')(newValue, 'y/MM/dd');
            $scope.dateOfBirth = $filter('date')(newValue, 'y/MM/dd HH:mm', 'UTC');
            $log.log( "Specific time UTC zone is : "+$scope.dateOfBirth );
            $log.log( "Date change newValue : "+newValue + " oldValue : "+oldValue );
        }, true);*/

        $scope.selectedDates = [];
        $scope.tags = [];
        var data = {tag:[]};

        $scope.wizadForm.dobIn = [];
        $scope.$watch('wizadForm.dobIn', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            if (newValue === null || newValue === undefined) { return; }

            var demoDate = $filter('date')(newValue, 'y/MM/dd');
            if( $scope.selectedDates.indexOf(demoDate)=== -1 ){
                $scope.selectedDates.push(demoDate);
                $scope.wizadForm.dob = $scope.selectedDates;
                data.tag.push( {value: demoDate} );
                $log.log(data.tag);
                $scope.tags = data.tag;
                //$log.log( "Date change newValue : "+newValue + " oldValue : "+$scope.wizadForm.dob );
            } else
                { return; }

        }, true);

        $scope.$watch('wizadForm.dob[0]', function(newVal, oldVal) {
            if (angular.equals(newVal, oldVal)) { return; }
            if (newVal === null || newVal === undefined) { return; }

            $log.log("Watch dob[0] :"+newVal);
            var demoDate = null;
            demoDate = $filter('date')(newVal, 'y/MM/dd');
            $scope.wizadForm.dob[0] = demoDate;
            $log.log("$scope.wizadForm.dob[0]: "+$scope.wizadForm.dob[0]);

        }, true);

        $scope.$watch('wizadForm.dob[1]', function(newVal, oldVal) {
            if (angular.equals(newVal, oldVal)) { return; }
            if (newVal === null || newVal === undefined) { return; }

            $log.log("Watch dob[1] :"+newVal);
            var demoDate = null;
            demoDate = $filter('date')(newVal, 'y/MM/dd');
            $scope.wizadForm.dob[1] = demoDate;
            $log.log("$scope.wizadForm.dob[1]: "+$scope.wizadForm.dob[1]);

        }, true);

        /*$scope.$watchGroup(['wizadForm.dob[0]', 'wizadForm.dob[1]'], function(newVal, oldVal) {
            if (angular.equals(newVal, oldVal)) { return; }
            if (newVal[0] === null || newVal[0] === undefined) { return; }
            if (newVal[1] === null || newVal[1] === undefined) { return; }

            $log.log("Watch group :"+newVal);
            var demoDate = null;

            if(newVal[0] !== oldVal[0]){
                demoDate = $filter('date')(newVal[0], 'y/MM/dd');
                $scope.wizadForm.dob[0] = demoDate;
                $log.log("$scope.wizadForm.dob[0]: "+$scope.wizadForm.dob[0]);
            }

            if(newVal[1] !== oldVal[1]){
                demoDate = $filter('date')(newVal[1], 'y/MM/dd');
                $scope.wizadForm.dob[1] = demoDate;
                $log.log("$scope.wizadForm.dob[1]: "+$scope.wizadForm.dob[1]);
            }

        },true);*/

        $scope.selectedDates2 = [];
        $scope.formAdvance.dobIn = [];
        $scope.$watch('formAdvance.dobIn', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }

            var demoDate = $filter('date')(newValue, 'y/MM/dd');
            if( $scope.selectedDates2.indexOf(demoDate)=== -1 ){
                //$log.log($scope.selectedDates.indexOf(demoDate));
                $scope.selectedDates2.push(demoDate);
                $scope.formAdvance.dob = $scope.selectedDates2;
                $log.log( "Date change newValue : "+newValue + " oldValue : "+$scope.formAdvance.dob );
            } else
            { return; }

        }, true);

        $scope.show2pickers = false;

        $scope.removeFromSelected = function(dt) {
            $scope.selectedDates.splice($scope.selectedDates.indexOf(dt), 1);
        };

        $scope.clearWizardFormArray = function(){
            $log.log($scope.wizadForm.isMarriedFilter);
            $scope.wizadForm.id.length = 0;
            $scope.wizadForm.dob.length = 0;
            $scope.wizadForm.salary.length = 0;
            $scope.wizadForm.username.length = 0;
            $scope.wizadForm.fullName.length = 0;
            $scope.wizadForm.gender.length = 0;
            $scope.wizadForm.isMarried.length = 0;
            $scope.wizadForm.dob.length = 0;
            $scope.wizadForm.dobIn.length = 0;
            $scope.selectedDates.length = 0;
            $scope.tags.length = 0;
            //$log.log("Clear All Date Array. "+$scope.wizadForm.dob, $scope.wizadForm.dobIn, $scope.selectedDates, $scope.tags);
        }

        $scope.clearformAdvanceArray = function(){
            $scope.formAdvance.id.length = 0;
            $scope.formAdvance.dob.length = 0;
            $scope.formAdvance.salary.length = 0;
            $scope.formAdvance.username.length = 0;
            $scope.formAdvance.fullName.length = 0;
            $scope.formAdvance.gender.length = 0;
            $scope.formAdvance.isMarried.length = 0;
            $scope.selectedDates2.length = 0;
            $scope.formAdvance.dobIn.length = 0;

            $scope.formAdvance.cityBasic = [];
            $scope.formAdvance.length=0;
            //$scope.formAdvance='';
        }

    });

    advanceList.controller('cityFilterController', function ($scope, $uibModal, $filter, advanceListService, $log, $q, $timeout) {
        $scope.cityForm = {};

        $scope.cityData = null;

        $scope.city_StringFilterList = [
            {id: 1, label: 'Equals'},
            {id: 2, label: 'Not Equals'},
            {id: 3, label: 'Start with'},
            {id: 4, label: 'Not Start with'},
            {id: 5, label: 'End with'},
            {id: 6, label: 'Not End with'},
            {id: 7, label: 'Contains'},
            {id: 8, label: 'Not Contains'},
            {id: 15, label: 'In'},
            {id: 16, label: 'Not In'},
            {id: 17, label: 'Null'},
            {id: 18, label: 'Not Null'},
            {id: 19, label: 'Sounds Like'}
        ];

        /* CSS Label Work */
        $scope.$watch('city_sortColumnName', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            $log.log(oldValue+"Col");
            document.getElementById(oldValue+"Col").classList.remove('selectedcolumn');
            document.getElementById(oldValue).className = "sort sort-btn";
            document.getElementById(newValue+"Col").className = "selectedcolumn";
            document.getElementById(newValue).classList.remove('sort-btn');
        }, true);

        /* Column Sorting Function */
        $scope.city_setColumnValue = function(name){

            if($scope.city_sortColumnName == name){
                if($scope.city_isAscending == true){
                    $scope.city_isAscending = false;

                } else {
                    $scope.city_isAscending = true;
                }
            } else {
                $scope.city_isAscending = true;
            }
            $scope.city_sortColumnName = name;
            $scope.city_getFormData();
        }

        $scope.city_firstRun = function(col){
            var colId = col+"Col";
            $scope.city_sortColumnName = col;
            $scope.city_isAscending = true;
            document.getElementById(colId).className = "selectedcolumn";
            document.getElementById(col).classList.remove('sort-btn');
        }
        //$scope.city_firstRun("city_id");

        $scope.cityForm.id = [];
        $scope.cityForm.name = [];
        $scope.cityForm.shortName = [];

        // I apply the remote data to the local scope.
        var city_filteredData = {};
        function city_applyRemoteData( cityData ) {
            city_filteredData = cityData;
            $scope.cityData = city_filteredData.data;

            $scope.city_currentPage = city_filteredData.currentPage;
            $scope.city_rowsPerPage = city_filteredData.rowsPerPage;
            $scope.city_totalPageCount = city_filteredData.totalPageCount;
            $scope.city_totalRowsCount = city_filteredData.totalRowsCount;
            $scope.city_displayCounter = city_filteredData.displayCounter;

            $scope.city_sortColumnName = "city_"+city_filteredData.sortColumnName;
            $log.log($scope.city_sortColumnName);
            $scope.city_isAscending = city_filteredData.isAscending;
        }
        // I load the remote data from the server.
        function city_loadListData() {
            advanceListService.getList().then(
                function( cityData ) {
                    city_applyRemoteData( cityData );
                }
            );
        }

        var filterTypeIds = {};
        var city_jsonData = {};

        var city_requestForData = null;
        $scope.city_abortRequest = function(){ return( city_requestForData && city_requestForData.abort() ); };

        $scope.city_isLoading = false;
        $scope.city_action = true;
        $scope.city_jsonData = function(data){
            filterTypeIds = {
                "name":data.nameFilter,
                "shortName":data.shortNameFilter
            };

            var y;
            for (var x in filterTypeIds) {
                if ( Object.prototype.hasOwnProperty.call(filterTypeIds,x)) {
                    y = filterTypeIds[x];
                    if (y==="null" || y===null || y==="" || typeof y === "undefined") {
                        delete filterTypeIds[x];
                    }
                }
            }

            city_jsonData = {
                "name":data.name,
                "shortName":data.shortName,
                "filterTypeId":filterTypeIds,
                "currentPage":$scope.city_currentPage,
                "rowsPerPage":$scope.city_rowsPerPage,
                "sortColumnName": $scope.city_sortColumnName,
                "isAscending":$scope.city_isAscending
            };
            $scope.city_isLoading = true;
            $scope.city_errorMassage = "";
            $scope.city_abortMassage = "";

            $scope.cityData = null;

            ( city_requestForData = advanceListService.listRequest( "/city/filter/json", city_jsonData ) ).then(
                function( cityData ) {
                    if(!cityData){
                        $log.warn("No Data have Seen!");
                    }else{
                        city_applyRemoteData(cityData);
                        $scope.city_isLoading = false;
                    }
                },
                function( errorMessage ) {
                    $log.warn( errorMessage );
                    if(angular.equals(errorMessage,"Request Aborted")){
                        $scope.city_abortMassage = "Request Aborted By User.";

                        $scope.city_currentPage = 1;
                        $scope.city_totalPageCount = 0;
                        $scope.city_totalRowsCount = 0;
                        $scope.city_displayCounter = "0 to 0 from 0";
                    } else {
                        $scope.city_errorMassage = errorMessage;
                    }
                    $scope.city_isLoading = false;
                }
            );

            $scope.items = ['item1', 'item2', 'item3'];
            $scope.animationsEnabled = true;

            $scope.open = function (size) {

                var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    size: size,
                    resolve: {
                        items: function () {
                            return $scope.items;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $scope.selected = selectedItem;
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.toggleAnimation = function () {
                $scope.animationsEnabled = !$scope.animationsEnabled;
            };
        }

        /*Genetare Page Number*/
        $scope.city_pageNumber = [1];
        $scope.$watch('city_totalPageCount', function(newValue, oldValue) {
            if (newValue === oldValue) { return; }
            $scope.city_pageNumber = [];
            for (var i = 1; i <= newValue; i++) {
                $scope.city_pageNumber.push(i);
            }
        }, true);

        $scope.maxSize = 5;
        $scope.city_CtrlPagingAct = function(currentPage){
            $scope.city_currentPage = currentPage;
            $scope.city_jsonData($scope.cityForm);
        }

        $scope.city_formName = "cityForm";
        $scope.city_getData = function(formName,form){
            $scope.city_currentPage = 1;
            $scope.city_formName = formName;
            $scope.city_jsonData(form);
            $log.log();
        }

        $scope.city_getFormData = function(){
            if($scope.city_formName === "cityForm"){
                $scope.city_jsonData($scope.cityForm);
            }
            if($scope.city_formName === ""){
                $scope.city_jsonData($scope.cityForm);
            }
        }

        //$scope.city_getData('cityForm',$scope.cityForm);

        $scope.city_resetFormWizard = function(){
            $('#cityForm_name').val('No Filter');
            $('#cityForm_shortName').val('No Filter');
            $('#cityForm_name').selectpicker('refresh');
            $('#cityForm_shortName').selectpicker('refresh');
        }

        $scope.city_clearWizardFormArray = function(){
            $scope.cityForm.name.length = 0;
            $scope.cityForm.shortName.length = 0;
        }

    });

    advanceList.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items) {

        $scope.items = items;
        $scope.selected = {
            item: $scope.items[0]
        };

        $scope.ok = function () {
            $uibModalInstance.close($scope.selected.item);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });

    // I act a repository for the remote list collection.
    /*advanceList.service("advanceListService",function( $http, $q ) {
        // Return public API.
        return({
            listRequest: listRequest
            //getList: getList
        });
        // ---
        // PUBLIC METHODS.
        // ---
        // I post a list with the given data to the remote collection.
        function listRequest( data ) {
            var request = $http.post("/test/filter/json",data);
            return( request.then( handleSuccess, handleError ) );
        }

        // I get all of the list in the remote collection.
        /!*function getList() {
         var request = $http.get("/data"); //assets/data/list.json
         return( request.then( handleSuccess, handleError ) );
         }*!/

        // ---
        // PRIVATE METHODS.
        // ---
        // I transform the error response, unwrapping the application dta from
        // the API response payload.
        function handleError( response ) {
            // The API response from the server should be returned in a
            // nomralized format. However, if the request was not handled by the
            // server (or what not handles properly - ex. server error), then we
            // may have to normalize it on our end, as best we can.
            if (! angular.isObject( response ) || ! response.data.message) {
                //return( $q.reject( "An unknown error occurred." ) );
                return( $q.reject( response.statusText + " ( Error code : "+response.status+")" ) );
            }
            // Otherwise, use expected error message.
            return( $q.reject( response.data.message ) );
        }
        // I transform the successful response, unwrapping the application data
        // from the API response payload.
        function handleSuccess( response ) {
            return( response.data );
        }
    });*/

    /*advanceList.factory("kcSleep", function($timeout, $log) {
        return function(ms) {
            return function(value) {
                return $timeout(function() {
                    $log.log("Hello Factory.");
                    return value;
                }, ms);
            };
        };
    });*/

    // I am the friend repository.
    advanceList.service( "advanceListService",
        function( $http, $q, $log ) {
            // I post a list with the given data to the remote collection.
            function listRequest(url, data) {
                // The timeout property of the http request takes a deferred value
                // that will abort the underying AJAX request if / when the deferred
                // value is resolved.
                var deferredAbort = $q.defer();

                // Initiate the AJAX request.
                var request = $http({
                    method: "post",
                    url: url,
                    data: data,
                    timeout: deferredAbort.promise
                });

                // Rather than returning the http-promise object, we want to pipe it
                // through another promise so that we can "unwrap" the response
                // without letting the http-transport mechansim leak out of the
                // service layer.
                var promise = request.then(
                    handleSuccess, handleError
                    /*function( response ) {
                        return( response.data );
                    },
                    function( response ) {
                        return( $q.reject( "Something went wrong" ) );
                    }*/
                );

                // Now that we have the promise that we're going to return to the
                // calling context, let's augment it with the abort method. Since
                // the $http service uses a deferred value for the timeout, then
                // all we have to do here is resolve the value and AngularJS will
                // abort the underlying AJAX request.
                promise.abort = function() {
                    deferredAbort.resolve();
                };
                // Since we're creating functions and passing them out of scope,
                // we're creating object references that may be hard to garbage
                // collect. As such, we can perform some clean-up once we know
                // that the requests has finished.
                promise.finally(
                    function() {
                        console.info( "Cleaning up object references." );
                        promise.abort = angular.noop;
                        deferredAbort = request = promise = null;
                    }
                );
                return( promise );
            }

            // ---
            // PRIVATE METHODS.
            // ---
            // I transform the error response, unwrapping the application dta from
            // the API response payload.
            function handleError( response ) {
                // The API response from the server should be returned in a
                // nomralized format. However, if the request was not handled by the
                // server (or what not handles properly - ex. server error), then we
                // may have to normalize it on our end, as best we can.
                if (! angular.isObject( response ) || ! response.data || ! response.data.message) {
                    //return( $q.reject( "An unknown error occurred." ) );
                    if(angular.equals(response.status, -1)){
                        return( $q.reject( "Request Aborted" ) );
                    }
                    return( $q.reject( response.statusText + " ( Error code : "+response.status+")" ) );
                }
                // Otherwise, use expected error message.
                return( $q.reject( response.data.message ) );
            }
            // I transform the successful response, unwrapping the application data
            // from the API response payload.
            function handleSuccess( response ) {
                return( response.data );
            }

            // Return the public API.
            return({
                listRequest: listRequest
            });
        }
    );

})(window.angular);