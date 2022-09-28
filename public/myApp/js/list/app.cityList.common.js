/**
 * Created by saif-dream on 10/3/2016.
 */
(function(angular){
    'use strict';

    angular.module('advanceList')
        .controller('CityController',
            function($scope, citySharedService, $filter, $log, maxSize,City, CityFilterTypeIds, ColumnProperties, PageProperties,
                     advanceListService, commonFilterList, stringSpFilterList, nullSpFilterList, rows, initialRowsPerPage)
            {
                $scope.rows = rows;
                $scope.rowsPerPage = initialRowsPerPage;

                $scope.StringFilterList = commonFilterList.concat(stringSpFilterList.concat(nullSpFilterList));
                $scope.modal = false;

                /* Application Initialization Start Here. */
                $scope.cityForm = new City([], [], []);
                $scope.cityFilterIds = new CityFilterTypeIds( '', '', '');

                $scope.cityPage = new PageProperties(1,10,0,0,"0 to 0 from 0");
                $scope.cityPage.setCurrentPage($scope.cityPage.currentPage);

                $scope.cityColumn = new ColumnProperties("id", true);

                /* CSS Label Work */
                $scope.$watch('cityColumn.sortColumnName', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    document.getElementById("city"+oldValue+"Col").classList.remove('selectedcolumn');
                    document.getElementById("city"+oldValue).className = "sort sort-btn";
                    document.getElementById("city"+newValue+"Col").className = "selectedcolumn";
                    document.getElementById("city"+newValue).classList.remove('sort-btn');
                }, true);

                /*Genetare Page Number*/
                $scope.cityPageNumber = [1];
                $scope.$watch('cityPage.totalPage', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    $scope.cityPageNumber = [];
                    for (var i = 1; i <= newValue; i++) { $scope.cityPageNumber.push(i); }
                }, true);

                $scope.firstRun = function(col){
                    var colId = "city"+col+"Col";
                    var col = "city"+col;
                    document.getElementById(colId).className = "selectedcolumn";
                    document.getElementById(col).classList.remove('sort-btn');
                }
                $scope.firstRun("id");
                /* Application Initialization End Here. */

                $scope.getCity = function( data, ft ) {
                    var postData = new City( data.id, data.name, data.shortName );
                    var filterType = new CityFilterTypeIds( ft.nameFilter, ft.shortNameFilter );
                    var pageData = $scope.cityPage.getJson();
                    var columnData = $scope.cityColumn.getJson();
                    var finalJsonData = angular.extend( postData.getJson(), filterType.getJson(), pageData, columnData );
                    var url = "/city/filter/json";
                    dataRequest ( url, finalJsonData );
                }

                var requestForData = null;
                $scope.abortCityRequest = function(){ return( requestForData && requestForData.abort() ); };

                function dataRequest ( url, finalJsonData ){
                    $scope.isLoading = true;
                    $scope.errorMassage = "";
                    $scope.abortMassage ="";
                    $scope.cityData = null;
                    ( requestForData = advanceListService.listRequest( url, finalJsonData ) ).then(
                        function( cityData ) {
                            if(!cityData){ $log.warn("No Data have Seen!");
                            } else{
                                applyRemoteData(cityData);
                                $scope.isLoading = false;
                            }
                        }, function( errorMessage ) {
                            $log.warn( errorMessage );
                            if(angular.equals(errorMessage,"Request Aborted")){
                                $scope.abortMassage = "Request Aborted By User.";
                                $scope.cityPage.setPage(1,0,0,"0 to 0 from 0");
                            } else { $scope.errorMassage = errorMessage; }
                            $scope.isLoading = false;
                        }
                    );
                }
                $scope.getCity($scope.cityForm, $scope.cityFilterIds);

                var ftData = {};
                function applyRemoteData( cityData ) {
                    $scope.isClick = false;
                    ftData = cityData;
                    $scope.cityData = ftData.data;
                    $scope.cityPage.setPage( ftData.currentPage,ftData.totalPageCount,ftData.totalRowsCount,ftData.displayCounter );
                    $scope.cityColumn.setColumn( ftData.sortColumnName,ftData.isAscending );
                }

                $scope.getFirstCityPage = function(data, ft){
                    $scope.cityPage.setCurrentPage(1);
                    $scope.getCity(data, ft);
                }

                $scope.cityCtrlPagingAct = function(page){
                    $scope.cityPage.setCurrentPage(page);
                    $scope.getCity($scope.cityForm,$scope.cityFilterIds);
                }

                $scope.addedCity = [];
                $scope.addRadioCity = function(id){
                    if($scope.addedCity.indexOf(id)=== -1){
                        $scope.addedCity.length = 0;
                        $scope.addedCity.push(id);
                    } return;
                }

                $scope.addInWizard = function(id){
                    if(id.length !== 0){
                        if($scope.wizardForm.city.indexOf(id[0])=== -1){ citySharedService.prepForBroadcast(id[0]); }
                    } return;
                }

                $scope.clearRadioField = function (){
                    var ele = document.getElementsByName("cityId");
                    for(var i=0; i<ele.length; i++){
                        if(ele[i].checked === true){
                            ele[i].checked = false;
                            return;
                        }
                    }
                }

                $scope.isClick = false;
                $scope.isClicked = function(value){ $scope.isClick = value; }
            }
        )
        .factory('City', function() {
            function City(id, name, shortName) {
                this.id = id;
                this.name = name;
                this.shortName = shortName;
            }
            City.prototype.getJson = function() {
                var jsonData = {
                    "name":this.name,
                    "shortName":this.shortName
                };
                return jsonData;
            }
            City.prototype.resetCity = function() {
                this.id = [];
                this.name = [];
                this.shortName = [];
            }
            return City;
        })
        .factory('CityFilterTypeIds', function() {
            function CityFilterTypeIds(nameFilter, shortNameFilter) {
                this.nameFilter = nameFilter;
                this.shortNameFilter = shortNameFilter;
            }
            CityFilterTypeIds.prototype.getJson = function() {
                var jsonData = {
                    "filterTypeId":{
                        "name":this.nameFilter,
                        "shortName":this.shortNameFilter,
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
            CityFilterTypeIds.prototype.resetFilterTypeIds = function() {
                this.nameFilter = null;
                this.shortNameFilter = null;
            }
            return CityFilterTypeIds;
        })
        .$inject = ['$scope', 'citySharedService'];
})(window.angular)