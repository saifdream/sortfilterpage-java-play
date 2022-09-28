/**
 * Created by saif-dream on 10/3/2016.
 */
(function(angular){
    'use strict';

    angular.module('advanceList')
        .controller('RoleController',
            function($scope, roleSharedService, $filter, $log, maxSize, Role, RoleFilterTypeIds, ColumnProperties,
                     PageProperties, advanceListService, commonFilterList, stringSpFilterList, nullSpFilterList, rows, initialRowsPerPage)
            {
                $scope.rows = rows;
                $scope.rowsPerPage = initialRowsPerPage;

                $scope.StringFilterList = commonFilterList.concat(stringSpFilterList.concat(nullSpFilterList));
                $scope.modal = false;
                /* Application Initialization Start Here. */

                $scope.roleForm = new Role([], [], [], []);
                $scope.roleFilterIds = new RoleFilterTypeIds('', '', '');

                $scope.rolePage = new PageProperties(1, 10, 0, 0, "0 to 0 from 0");
                $scope.rolePage.setCurrentPage($scope.rolePage.currentPage);

                $scope.roleColumn = new ColumnProperties("id", true);

                /* CSS Label Work */
                $scope.$watch('roleColumn.sortColumnName', function (newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    document.getElementById("role" + oldValue + "Col").classList.remove('selectedcolumn');
                    document.getElementById("role" + oldValue).className = "sort sort-btn";
                    document.getElementById("role" + newValue + "Col").className = "selectedcolumn";
                    document.getElementById("role" + newValue).classList.remove('sort-btn');
                }, true);

                /*Genetare Page Number*/
                $scope.rolePageNumber = [1];
                $scope.$watch('rolePage.totalPage', function (newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    $scope.rolePageNumber = [];
                    for (var i = 1; i <= newValue; i++) { $scope.rolePageNumber.push(i); }
                }, true);

                $scope.firstRun = function (col) {
                    var colId = "role" + col + "Col";
                    var col = "role" + col;
                    document.getElementById(colId).className = "selectedcolumn";
                    document.getElementById(col).classList.remove('sort-btn');
                }
                $scope.firstRun("id");
                /* Application Initialization End Here. */

                $scope.getRole = function (data, ft) {
                    var postData = new Role(data.id, data.name, data.description, data.type);
                    var filterType = new RoleFilterTypeIds(ft.nameFilter, ft.descriptionFilter, ft.typeFilter);
                    var pageData = $scope.rolePage.getJson();
                    var columnData = $scope.roleColumn.getJson();
                    var finalJsonData = angular.extend(postData.getJson(), filterType.getJson(), pageData, columnData);

                    var url = "/role/filter/json";
                    dataRequest(url, finalJsonData);
                }
                function dataRequest(url, finalJsonData) {
                    $scope.isLoading = true;
                    $scope.errorMassage = "";
                    $scope.abortMassage = "";
                    $scope.roleData = null;
                    var requestForData = null;
                    $scope.abortRoleRequest = function () { return ( requestForData && requestForData.abort() ) };

                    ( requestForData = advanceListService.listRequest(url, finalJsonData) ).then(
                        function (roleData) {
                            if (!roleData) { $log.warn("No Data have Seen!");
                            } else {
                                applyRemoteData(roleData);
                                $scope.isLoading = false;
                            }
                        }, function (errorMessage) {
                            $log.warn(errorMessage);
                            if (angular.equals(errorMessage, "Request Aborted")) {
                                $scope.abortMassage = "Request Aborted By User.";
                                $scope.rolePage.setPage(1, 0, 0, "0 to 0 from 0");
                            } else {  $scope.errorMassage = errorMessage; }
                            $scope.isLoading = false;
                        }
                    );
                }

                $scope.getRole($scope.roleForm, $scope.roleFilterIds);

                var ftData = {};
                function applyRemoteData(roleData) {
                    $scope.isClick = false;
                    ftData = roleData;
                    $scope.roleData = ftData.data;
                    $scope.rolePage.setPage(ftData.currentPage, ftData.totalPageCount, ftData.totalRowsCount, ftData.displayCounter);
                    $scope.roleColumn.setColumn(ftData.sortColumnName, ftData.isAscending);
                }

                $scope.getFirstRolePage = function (data, ft) {
                    $scope.rolePage.setCurrentPage(1);
                    $scope.getRole(data, ft);
                }

                $scope.roleCtrlPagingAct = function (page) {
                    $scope.rolePage.setCurrentPage(page);
                    $scope.getRole($scope.roleForm, $scope.roleFilterIds);
                }

                $scope.addedRole = [];
                $scope.addRadioRole = function (id) {
                    if ($scope.addedRole.indexOf(id) === -1) {
                        $scope.addedRole.length = 0;
                        $scope.addedRole.push(id);
                    } return;
                }
                $scope.addInWizard = function (id) {
                    if (id.length !== 0) {
                        if ($scope.wizardForm.role.indexOf(id[0]) === -1) { roleSharedService.prepForBroadcast(id[0]); }
                    } return;
                }

                $scope.clearRadioField = function () {
                    var ele = document.getElementsByName("roleName");
                    for(var i=0; i<ele.length; i++){
                        if(ele[i].checked === true){ ele[i].checked = false; return; }
                    }
                }
                $scope.isClick = false;
                $scope.isClicked = function (value) { $scope.isClick = value; }
            }
        )
        .factory( "Role", function() {
            function Role(id, name, description, type) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.type = type;
            }
            Role.prototype.getJson = function() {
                var jsonData = {
                    //"id":this.id,
                    "name":this.name,
                    "description":this.description,
                    "type":this.type
                };
                return jsonData;
            }
            Role.prototype.resetRole = function() {
                this.id = [];
                this.name = [];
                this.description = [];
                this.type = [];
            }
            return Role;
        })
        .factory( "RoleFilterTypeIds", function() {
            function RoleFilterTypeIds( nameFilter, descriptionFilter, typeFilter ) {
                this.nameFilter = nameFilter;
                this.descriptionFilter = descriptionFilter;
                this.typeFilter = typeFilter;
            }
            RoleFilterTypeIds.prototype.getJson = function() {
                var jsonData = {
                    "filterTypeId":{
                        "name":this.nameFilter,
                        "description":this.descriptionFilter,
                        "type":this.typeFilter
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
                } return jsonData
            }
            RoleFilterTypeIds.prototype.resetFilterTypeIds = function() {
                this.nameFilter = null;
                this.descriptionFilter = null;
                this.typeFilter = null;
            }
            return RoleFilterTypeIds;
        })
        .$inject = ['$scope', 'roleSharedService'];
})(window.angular)