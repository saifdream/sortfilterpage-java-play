/**
 * Created by saif-dream on 10/3/2016.
 */
(function(angular){
    'use strict';

    angular.module('advanceList')
        .controller('AdvanceFilterController',
            function($scope, citySharedService, roleSharedService, $filter, $compile, $log, genderList, maritalStatus, maxSize,
                     FormProperties, FilterTypeIds, ColumnProperties, PageProperties, stringToArray, advanceListService,
                     commonFilterList, stringSpFilterList, numberSpFilterList, nullSpFilterList, rows, initialRowsPerPage,
                     FilterTypeService)
            {
                /* Accordion Start */
                $scope.oneAtATime = true;
                $scope.isOpen = true;
                $scope.status = { openAccordion: true, isFirstOpen: true };
                /* Accordion End */

                /* Date Properties Start Here */
                $scope.today = function() { $scope.dob = new Date(); };
                //$scope.today();

                $scope.clear = function() { $scope.dob = null; };

                $scope.inlineOptions = { customClass: getDayClass, minDate: new Date(), showWeeks: false };

                $scope.dateOptions = {
                    //dateDisabled: disabled,
                    formatYear: 'yy',
                    maxDate: new Date(2030, 5, 22),
                    minDate: new Date(),
                    startingDay: 1
                };

                // Disable weekend selection
                function disabled(data) {
                    var date = data.date, mode = data.mode;
                    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
                }

                $scope.toggleMin = function() {
                    $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
                    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
                };
                $scope.toggleMin();

                $scope.open1 = function() { $scope.popup1.opened = true; };

                $scope.open2 = function() { $scope.popup2.opened = true; };

                $scope.setDate = function(year, month, day) { $scope.dob = new Date(year, month, day); };

                $scope.formats = ['y/MM/dd','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
                $scope.format = $scope.formats[0];
                $scope.altInputFormats = ['M!/d!/yyyy'];

                $scope.popup1 = { opened: false };
                $scope.popup2 = { opened: false };

                var tomorrow = new Date();
                tomorrow.setDate(tomorrow.getDate() + 1);
                var afterTomorrow = new Date();
                afterTomorrow.setDate(tomorrow.getDate() + 1);
                $scope.events = [{ date: tomorrow, status: 'full' }, { date: afterTomorrow, status: 'partially' } ];

                function getDayClass(data) {
                    var date = data.date, mode = data.mode;
                    if (mode === 'day') {
                        var dayToCheck = new Date(date).setHours(0,0,0,0);
                        for (var i = 0; i < $scope.events.length; i++) {
                            var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);
                            if (dayToCheck === currentDay) { return $scope.events[i].status; }
                        }
                    } return '';
                }
                /* Date Properties End Here */

                /* Application Initialization Start Here. */
                var stringSpecial = commonFilterList.concat(stringSpFilterList);
                $scope.CommonFilterList = commonFilterList;
                $scope.UserNameFilterList = stringSpecial;
                $scope.StringFilterList = stringSpecial.concat(nullSpFilterList);
                $scope.NumberFilterList = commonFilterList.concat(numberSpFilterList.concat(nullSpFilterList));
                //$scope.DateFilterList = $scope.NumberFilterList;
                $scope.genderList = genderList;
                $scope.maritalStatus = maritalStatus;

                $scope.maxSize = maxSize;
                $scope.rows = rows;
                $scope.rowsPerPage = initialRowsPerPage;

                $scope.wizardForm = new FormProperties([], [], [], [], [], [], [], [], []);
                $scope.formWizardFilterIds = new FilterTypeIds( '', '', '', '', '', '', '', '');

                $scope.formAdvance = new FormProperties([], [], [], [], [], [], [], []);
                $scope.formAdvanceFilterIds = new FilterTypeIds( '', '', '', '', '', '', '');

                $scope.listPage = new PageProperties(1,10,0,0,"0 to 0 from 0");
                $scope.listPage.setCurrentPage($scope.listPage.currentPage);

                //$scope.addedCity = [];
                $scope.addedRole = [];

                $scope.listColumn = new ColumnProperties("id", true);
                $scope.listData = null;
                $scope.isLoading = false;

                $scope.firstRun = function(col){
                    var colId = col+"Col";
                    document.getElementById(colId).className = "selectedcolumn";
                    document.getElementById(col).classList.remove('sort-btn');
                }
                $scope.firstRun("id");
                /* Application Initialization End Here. */

                /* Watch Department Start Here */
                /* CSS Label Work */
                $scope.$watch('listColumn.sortColumnName', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    //$log.log("sortColumnName: "+newValue);
                    document.getElementById(oldValue+"Col").classList.remove('selectedcolumn');
                    document.getElementById(oldValue).className = "sort sort-btn";
                    document.getElementById(newValue+"Col").className = "selectedcolumn";
                    document.getElementById(newValue).classList.remove('sort-btn');
                }, true);

                /*Genetare Page Number*/
                $scope.pageNumber = [1];
                $scope.$watch('listPage.totalPage', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    $scope.pageNumber = [];
                    for (var i = 1; i <= newValue; i++) { $scope.pageNumber.push(i); }
                }, true);

                $scope.wizardForm.dobIn = [];
                $scope.wizardForm.dob = [];
                function updateDobCollection (dob) {
                    if ($scope.wizardForm.dob.indexOf(dob) === -1) { $scope.wizardForm.dob.push(dob);
                    } else if ($scope.wizardForm.dob.indexOf(dob) > -1) { return; }
                }

                $scope.getSetDateRange = function () {
                    var dateFrom = angular.element(document.querySelector('#dateFrom')).val();
                    var dateTo = angular.element(document.querySelector('#dateTo')).val();
                    if(angular.equals(dateFrom, "") && angular.equals(dateTo, "") || angular.equals(dateFrom, undefined)|| angular.equals(dateTo, undefined)){
                        return;
                    }
                    dateFrom = $filter('date')(dateFrom, 'y/MM/dd');
                    dateTo = $filter('date')(dateTo, 'y/MM/dd');

                    updateDobCollection(dateFrom);
                    if ($scope.wizardForm.dob[1] !== dateTo) {
                        $scope.wizardForm.dob.push(dateTo);
                    }
                }

                $scope.getSetMultiDate = function () {
                    var dateString = angular.element(document.querySelector('#multipleDate')).val();
                    if(angular.equals(dateString, "") || angular.equals(dateString, undefined)){ return; }
                    angular.forEach(stringToArray.getStringArray(dateString,","), function(genre, index){
                        updateDobCollection($filter('date')(genre, 'y/MM/dd'));
                    })
                }

                $scope.getSetDobDate = function(){
                    var dobDate = angular.element(document.querySelector('#dobDate')).val();
                    if(angular.equals(dobDate, "") || angular.equals(dobDate, undefined)){ return; }
                    dobDate = $filter('date')(dobDate, 'y/MM/dd');
                    $scope.wizardForm.dob[0] = dobDate;
                }

                $scope.getSetDate = function () {
                    if(angular.equals($scope.formWizardFilterIds.dobFilter, "13") ||
                        angular.equals($scope.formWizardFilterIds.dobFilter, "14")){
                        $scope.getSetDateRange();
                        console.log("Date Range Selected.");
                    } else if(angular.equals($scope.formWizardFilterIds.dobFilter, "15") ||
                        angular.equals($scope.formWizardFilterIds.dobFilter, "16")){
                        $scope.getSetMultiDate();
                    } else { $scope.getSetDobDate(); }
                }

                $scope.$watch('formAdvance.username', function(newValue, oldValue) {
                    if(newValue === oldValue){return;}
                    if(angular.isUndefined(newValue[0])){
                        $('#wizardForm_username').selectpicker('val', '');
                        $('#wizardForm_username').selectpicker('refresh');
                        $scope.formWizardFilterIds.usernameFilter = '';
                        $scope.wizardForm.username = [];
                        return;
                    }
                    if(newValue[0].length > 1){
                        var id = FilterTypeService.getFilterId("Username",newValue);
                        console.log(id)
                        $('#wizardForm_username').selectpicker('val', id);
                        $('#wizardForm_username').selectpicker('refresh');
                        $scope.formWizardFilterIds.usernameFilter = id;
                        newValue[0] = newValue[0].replace(/[^\w\s]/gi, '');
                        $scope.wizardForm.username = newValue;
                    }
                });

                $scope.$watch('formAdvance.fullName', function(newValue, oldValue) {
                    if(newValue === oldValue){return;}
                    if(angular.isUndefined(newValue[0])){
                        $('#wizardForm_fullName').selectpicker('val', '');
                        $('#wizardForm_fullName').selectpicker('refresh');
                        $scope.formWizardFilterIdsfullNameFilter = '';
                        $scope.wizardForm.fullName = [];
                        return;
                    }
                    if(newValue[0].length > 1){
                        var id = FilterTypeService.getFilterId("String",newValue);
                        console.log(id)
                        $('#wizardForm_fullName').selectpicker('val', id);
                        $('#wizardForm_fullName').selectpicker('refresh');
                        $scope.formWizardFilterIds.fullNameFilter = id;
                        newValue[0] = newValue[0].replace(/[^\w\s]/gi, '');
                        $scope.wizardForm.fullName = newValue;
                    }
                });

                $scope.$watch('formAdvance.salary', function(newValue, oldValue) {
                    if(newValue === oldValue){return;}
                    if(angular.isUndefined(newValue[0])){
                        $('#wizardForm_salary').selectpicker('val', '');
                        $('#wizardForm_salary').selectpicker('refresh');
                        $scope.formWizardFilterIdssalaryFilter = '';
                        $scope.wizardForm.salary = [];
                        return;
                    }
                    if(newValue[0].length > 1){
                        var id = FilterTypeService.getFilterId("Number",newValue);
                        $('#wizardForm_salary').selectpicker('val', id);
                        $('#wizardForm_salary').selectpicker('refresh');
                        $scope.formWizardFilterIds.salaryFilter = id;
                        if(id === 13 || id === 14){
                            var array = newValue[0].split("-");
                            newValue = array;
                        }
                        newValue[0] = newValue[0].replace(/[^\w\s]/gi, '');
                        $scope.wizardForm.salary = newValue;
                    }
                });

                $scope.$watch('formAdvance.dob', function(newValue, oldValue) {
                    if(newValue === oldValue){return;}
                    if(angular.isUndefined(newValue[0])){
                        $('#wizardForm_dob').selectpicker('val', '');
                        $('#wizardForm_dob').selectpicker('refresh');
                        $scope.formWizardFilterIdsdobFilter = '';
                        $scope.wizardForm.dob = [];
                        return;
                    }
                    if(newValue[0].length > 1){
                        var id = FilterTypeService.getFilterId("Date",newValue);
                        console.log(id)
                        $('#wizardForm_dob').selectpicker('val', id);
                        $('#wizardForm_dob').selectpicker('refresh');
                        $scope.formWizardFilterIds.dobFilter = id;
                        $scope.wizardForm.dob = newValue;
                    }
                });

                /* Watch Department End Here */

                var requestForData = null;
                $scope.abortRequest = function(){ return( requestForData && requestForData.abort() ) };

                function isEmpty(value) { return value!==''; }
                $scope.getList = function( data, ft ) {
                    var postData = new FormProperties(
                        data.id.filter(isEmpty),
                        data.role.filter(isEmpty),
                        data.username.filter(isEmpty),
                        data.fullName.filter(isEmpty),
                        data.gender.filter(isEmpty),
                        data.isMarried.filter(isEmpty),
                        data.dob.filter(isEmpty),
                        data.salary.filter(isEmpty),
                        data.city.filter(isEmpty)
                    );
                    var filterType = new FilterTypeIds( ft.roleFilter, ft.usernameFilter, ft.fullNameFilter, ft.dobFilter, ft.genderFilter, ft.isMarriedFilter, ft.salaryFilter, ft.cityFilter );
                    var pageData = $scope.listPage.getJson();
                    var columnData = $scope.listColumn.getJson();
                    var finalJsonData = angular.extend(postData.getJson(), filterType.getJson(), pageData, columnData);
                    var url = "/test/filter/json";
                    dataRequest ( url, finalJsonData );
                }
                function dataRequest ( url, finalJsonData ){
                    $scope.isLoading = true;
                    $scope.errorMassage = "";
                    $scope.abortMassage = "";
                    $scope.listData = null;
                    ( requestForData = advanceListService.listRequest( url, finalJsonData ) ).then(
                        function( listData ) {
                            if(!listData){ $log.warn("No Data have Seen!");
                            } else{
                                applyRemoteData(listData);
                                $scope.isLoading = false;
                            }
                        }, function( errorMessage ) {
                            $log.warn( errorMessage );
                            if(angular.equals(errorMessage,"Request Aborted")){
                                $scope.abortMassage = "Request Aborted By User.";
                                $scope.listPage.setPage(1,0,0,"0 to 0 from 0");
                            } else { $scope.errorMassage = errorMessage;}
                            $scope.isLoading = false;
                        }
                    );
                }
                $scope.getList($scope.wizardForm, $scope.formWizardFilterIds);

                var ftData = {};
                function applyRemoteData( listData ) {
                    ftData = listData;
                    $scope.listData = ftData.data;
                    $scope.listPage.setPage( ftData.currentPage,ftData.totalPageCount,ftData.totalRowsCount,ftData.displayCounter );
                    $scope.listColumn.setColumn( ftData.sortColumnName,ftData.isAscending );
                }

                $scope.getFirstPage = function( data, ft ){
                    $scope.listPage.setCurrentPage(1);
                    $scope.getList( data, ft );
                }

                $scope.listCtrlPagingAct = function(page){
                    $scope.listPage.setCurrentPage(page);
                    $scope.getList($scope.wizardForm,$scope.formWizardFilterIds);
                }

                $scope.resetCityModel = function(){
                    $scope.wizardForm.city=[];
                    $('#wizardForm_cityModel').selectpicker('val', []);
                    $('#wizardForm_cityModel').selectpicker('refresh');
                    $('#wizardForm_singleCityModel').selectpicker('val', []);
                    $('#wizardForm_singleCityModel').selectpicker('refresh');
                    $scope.addedCity = [];
                }

                $scope.resetRoleModel = function(){
                    $scope.wizardForm.city=[];
                    $('#wizardForm_roleModel').selectpicker('val', []);
                    $('#wizardForm_roleModel').selectpicker('refresh');
                    $('#wizardForm_singleRoleModel').selectpicker('val', []);
                    $('#wizardForm_singleRoleModel').selectpicker('refresh');
                    $scope.addedRole = [];
                }

                /* City Micro Search Value Setter */
                $scope.isMicroSearchActive = false;
                $scope.isDropdownSelected = false;
                $scope.mocroSearchCunter = 0;

                $scope.setSingleCity = function(city){
                    if($scope.wizardForm.city.length !== 0){
                        $scope.wizardForm.city.length = 0;
                        $scope.wizardForm.city.push(city);
                        return;
                    }
                    $scope.wizardForm.city.push(city);
                }

                $scope.cityArr = [];
                $scope.newArr = [];
                function updateWizardCity(city) {
                    $scope.newArr = city;
                    if ($scope.newArr.length === 0){
                        $scope.isDropdownSelected = false;
                        $scope.isMicroSearchActive = false;
                        return;
                    }
                    if ($scope.isMicroSearchActive === true && $scope.cityArr.length !== 0){
                        $scope.newArr = $scope.newArr.concat($scope.cityArr);
                        $('#wizardForm_cityModel').selectpicker('val', $scope.newArr);
                        $('#wizardForm_cityModel').selectpicker('refresh');
                        $scope.cityArr.length = 0;
                        $scope.isMicroSearchActive = false;
                        $scope.wizardForm.city = $('#wizardForm_cityModel').val();
                        return;
                    }
                }
                $scope.setCity = function(city) {
                    $scope.isDropdownSelected = true;
                    updateWizardCity(city);
                }
                $scope.$on('handleCityBroadcast', function() {
                    var cityId = citySharedService.city;
                    if($scope.formWizardFilterIds.cityFilter == 1 || $scope.formWizardFilterIds.cityFilter == 2){
                        $scope.wizardForm.city.length = 0;
                        $scope.wizardForm.city.push(cityId);
                        $('#wizardForm_singleCityModel').selectpicker('val', cityId);
                        $('#wizardForm_singleCityModel').selectpicker('refresh');
                        return;
                    }
                    if($scope.cityArr.indexOf(cityId)=== -1 && $scope.wizardForm.city.indexOf(cityId) === -1) {
                        $scope.cityArr.push(cityId);
                        if($scope.wizardForm.city.length !== 0){
                            $scope.wizardForm.city = $scope.cityArr.concat($('#wizardForm_cityModel').val());
                        } else {
                            $scope.wizardForm.city = $scope.cityArr;
                        }
                        $('#wizardForm_cityModel').selectpicker('val', $scope.wizardForm.city);
                        $('#wizardForm_cityModel').selectpicker('refresh');
                        $scope.isMicroSearchActive = true;
                    }
                    return;
                });

                /* Role Micro Search Value Setter */
                $scope.isRoleMicroSearchActive = false;
                $scope.isRoleDropdownSelected = false;

                $scope.setSingleRole = function(role){
                    //$log.log("I am in setSingleRole().");
                    if($scope.wizardForm.role.length !== 0){
                        $scope.wizardForm.role.length = 0;
                        $scope.wizardForm.role.push(role);
                        return;
                    }
                    $scope.wizardForm.role.push(role);
                }

                $scope.roleArr = [];
                $scope.newRoleArr = [];
                function updateWizardRole(role) {
                    $scope.newRoleArr = role;
                    if ($scope.newRoleArr.length === 0){
                        $scope.isRoleDropdownSelected = false;
                        $scope.isRoleMicroSearchActive = false;
                        return;
                    }
                    if ($scope.isRoleMicroSearchActive === true && $scope.roleArr.length !== 0){
                        $scope.newRoleArr = $scope.newRoleArr.concat($scope.roleArr);
                        $('#wizardForm_roleModel').selectpicker('val', $scope.newRoleArr);
                        $('#wizardForm_roleModel').selectpicker('refresh');
                        $scope.roleArr.length = 0;
                        $scope.isRoleMicroSearchActive = false;
                        $scope.wizardForm.role = $('#wizardForm_roleModel').val();
                        return;
                    }
                }
                $scope.setRole = function(role) {
                    $scope.isRoleDropdownSelected = true;
                    updateWizardRole(role);
                }
                $scope.$on('handleRoleBroadcast', function() {
                    var roleId = roleSharedService.role;
                    if($scope.formWizardFilterIds.roleFilter == 1 || $scope.formWizardFilterIds.roleFilter == 2){
                        $scope.wizardForm.role.length = 0;
                        $scope.wizardForm.role.push(roleId);
                        $('#wizardForm_singleRoleModel').selectpicker('val', roleId);
                        $('#wizardForm_singleRoleModel').selectpicker('refresh');
                        return;
                    }
                    if($scope.roleArr.indexOf(roleId)=== -1 && $scope.wizardForm.role.indexOf(roleId) === -1) {
                        $scope.roleArr.push(roleId);
                        if($scope.wizardForm.role.length !== 0){
                            $scope.wizardForm.role = $scope.roleArr.concat($('#wizardForm_roleModel').val());
                        } else {
                            $scope.wizardForm.role = $scope.roleArr;
                        }
                        $('#wizardForm_roleModel').selectpicker('val', $scope.wizardForm.role);
                        $('#wizardForm_roleModel').selectpicker('refresh');
                        $scope.isRoleMicroSearchActive = true;
                    }
                    return;
                });
            }
        )
        .value('genderList', [{value: "M", label: 'Male'}, {value: "F", label: 'Female'}])
        .value('maritalStatus', [{value: true, label: 'Married'}, {value: false, label: 'Unmarried'}])
        .factory( "FormProperties", function() {
            function FormProperties(id, role, username, fullName, gender, isMarried, dob, salary, city) {
                this.id = id;
                this.role = role;
                this.username = username;
                this.fullName = fullName;
                this.gender = gender;
                this.isMarried = isMarried;
                this.dob = dob;
                this.salary = salary;
                this.city = city;
            }
            FormProperties.prototype.getJson = function() {
                var jsonData = {
                    "username":this.username,
                    "fullName":this.fullName,
                    "dob":this.dob,
                    "gender":this.gender,
                    "isMarried":this.isMarried,
                    "salary":this.salary,
                    "city":this.city,
                    "role":this.role
                };
                return jsonData;
            }
            FormProperties.prototype.resetForm = function() {
                this.id = [];
                this.role = [];
                this.username = [];
                this.fullName = [];
                this.gender = [];
                this.isMarried = [];
                this.dob = [];
                this.salary = [];
                this.city = [];
            }
            return FormProperties;
        })
        .factory( "FilterTypeIds", function() {
            function FilterTypeIds(roleFilter, usernameFilter, fullNameFilter, dobFilter, genderFilter, isMarriedFilter, salaryFilter, cityFilter) {
                this.roleFilter = roleFilter;
                this.usernameFilter = usernameFilter;
                this.fullNameFilter = fullNameFilter;
                this.dobFilter = dobFilter;
                this.genderFilter = genderFilter;
                this.isMarriedFilter = isMarriedFilter;
                this.salaryFilter = salaryFilter;
                this.cityFilter = cityFilter;
            }
            FilterTypeIds.prototype.getJson = function() {
                var jsonData = {
                    "filterTypeId":{
                        "username":this.usernameFilter,
                        "fullName":this.fullNameFilter,
                        "dob":this.dobFilter,
                        "gender":this.genderFilter,
                        "isMarried":this.isMarriedFilter,
                        "salary":this.salaryFilter,
                        "city":this.cityFilter,
                        "role":this.roleFilter
                    }
                };
                var y;
                for (var x in jsonData.filterTypeId) {
                    if ( Object.prototype.hasOwnProperty.call(jsonData.filterTypeId,x)) {
                        y = jsonData.filterTypeId[x];
                        if (y==="null" || y===null || y==="" || typeof y === "undefined") {
                            delete jsonData.filterTypeId[x];
                        }
                    }
                }
                return jsonData
            }
            FilterTypeIds.prototype.resetFilterTypeIds = function() {
                this.idFilter = null;
                this.roleFilter = null;
                this.usernameFilter = null;
                this.fullNameFilter = null;
                this.dobFilter = null;
                this.genderFilter = null;
                this.isMarriedFilter = null;
                this.salaryFilter = null;
                this.cityFilter = null;
            }
            return FilterTypeIds;
        })
        .$inject = ['$scope', 'citySharedService', 'roleSharedService'];
})(window.angular)