/**
 * Created by saif-dream on 10/3/2016.
 */
(function(angular){
    'use strict';

    angular.module('advanceList')
        .controller('AddressController',
            function($scope, addressSharedService, $filter, $log, maxSize, Address, AddressFilterTypeIds, ColumnProperties,
                     PageProperties, advanceListService, commonFilterList, stringSpFilterList, nullSpFilterList, rows, initialRowsPerPage)
            {
                $scope.rows = rows;
                $scope.rowsPerPage = initialRowsPerPage;

                $scope.StringFilterList = commonFilterList.concat(stringSpFilterList.concat(nullSpFilterList));
                $scope.modal = false;

                /* Application Initialization Start Here. */
                $scope.addressForm = new Address([], [], [], []);
                $scope.addressFilterIds = new AddressFilterTypeIds( '', '', '');

                $scope.addressPage = new PageProperties(1,10,0,0,"0 to 0 from 0");
                $scope.addressPage.setCurrentPage($scope.addressPage.currentPage);

                $scope.addressColumn = new ColumnProperties("id", true);

                /* CSS Label Work */
                $scope.$watch('addressColumn.sortColumnName', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    document.getElementById("address" + oldValue +"Col").classList.remove('selectedcolumn');
                    document.getElementById("address" + oldValue).className = "sort sort-btn";
                    document.getElementById("address" + newValue + "Col").className = "selectedcolumn";
                    document.getElementById("address" + newValue).classList.remove('sort-btn');
                }, true);

                /*Genetare Page Number*/
                $scope.addressPageNumber = [1];
                $scope.$watch('addressPage.totalPage', function(newValue, oldValue) {
                    if (newValue === oldValue) { return; }
                    $scope.addressPageNumber = [];
                    for (var i = 1; i <= newValue; i++) { $scope.addressPageNumber.push(i); }
                }, true);

                $scope.firstRun = function(col){
                    var colId = "address"+col+"Col";
                    var col = "address"+col;
                    document.getElementById(colId).className = "selectedcolumn";
                    document.getElementById(col).classList.remove('sort-btn');
                }
                $scope.firstRun("id");
                /* Application Initialization End Here. */

                $scope.getAddress = function( data, ft ) {
                    var postData = new Address( data.id, data.name, data.description, data.type );
                    var filterType = new AddressFilterTypeIds( ft.nameFilter, ft.descriptionFilter, ft.typeFilter );
                    var pageData = $scope.addressPage.getJson();
                    var columnData = $scope.addressColumn.getJson();
                    var finalJsonData = angular.extend( postData.getJson(), filterType.getJson(), pageData, columnData );

                    var url = "/address/filter/json";
                    dataRequest ( url, finalJsonData );
                }
                function dataRequest ( url, finalJsonData ){
                    $scope.isLoading = true;
                    $scope.errorMassage = "";
                    $scope.abortMassage = "";
                    $scope.addressData = null;
                    var requestForData = null;
                    $scope.abortAddressRequest = function(){ return( requestForData && requestForData.abort() ); };

                    ( requestForData = advanceListService.listRequest( url, finalJsonData ) ).then(
                        function( addressData ) {
                            if(!addressData){ $log.warn("No Data have Seen!");
                            } else{
                                applyRemoteData(addressData);
                                $scope.isLoading = false;
                            }
                        },
                        function( errorMessage ) {
                            $log.warn( errorMessage );
                            if(angular.equals(errorMessage,"Request Aborted")){
                                $scope.abortMassage = "Request Aborted By User.";
                                $scope.addressPage.setPage(1,0,0,"0 to 0 from 0");
                            } else { $scope.errorMassage = errorMessage; }
                            $scope.isLoading = false;
                        }
                    );
                }
                $scope.getAddress($scope.addressForm, $scope.addressFilterIds);

                var ftData = {};
                function applyRemoteData( addressData ) {
                    $scope.isClick = false;
                    ftData = addressData;
                    $scope.addressData = ftData.data;
                    $scope.addressPage.setPage( ftData.currentPage,ftData.totalPageCount,ftData.totalRowsCount,ftData.displayCounter );
                    $scope.addressColumn.setColumn( ftData.sortColumnName,ftData.isAscending );
                }

                $scope.getFirstAddressPage = function( data, ft ){
                    $scope.addressPage.setCurrentPage(1);
                    $scope.getAddress( data, ft );
                }

                $scope.addressCtrlPagingAct = function(page){
                    $scope.addressPage.setCurrentPage(page);
                    $scope.getAddress($scope.addressForm,$scope.addressFilterIds);
                }

                $scope.addedAddress = [];
                $scope.addRadioAddress = function(id){
                    if($scope.addedAddress.indexOf(id)=== -1){
                        $scope.addedAddress.length = 0;
                        $scope.addedAddress.push(id);
                    } return;
                }

                $scope.addInWizard = function(id){
                    if(id.length !== 0){
                        if($scope.wizardForm.address.indexOf(id[0])=== -1){ addressSharedService.prepForBroadcast(id[0]); }
                    } return;
                }

                $scope.checkAddress = function(id){
                    if($scope.addedAddress.indexOf(id)=== -1){ return false;
                    } else return true;
                }

                $scope.isClick = false;
                $scope.isClicked = function(value){ $scope.isClick = value; }
            }
        )
        .factory( "Address", function() {
            function Address(id, roadNo, roadName, houseNo, houseName, apartmentNo, flatNo, area) {
                this.id = id;
                this.roadNo = roadNo;
                this.roadName = roadName;
                this.houseNo = houseNo;
                this.houseName = houseName;
                this.apartmentNo = apartmentNo;
                this.flatNo = flatNo;
                this.area = area;
            }
            Address.prototype.getJson = function() {
                var jsonData = {
                    //"id":this.id,
                    "roadNo":this.roadNo,
                    "roadName":this.roadName,
                    "houseNo":this.houseNo,
                    "houseName":this.houseName,
                    "apartmentNo":this.apartmentNo,
                    "flatNo":this.flatNo,
                    "area":this.area
                };
                return jsonData;
            }
            Address.prototype.resetRole = function() {
                this.id = [];
                this.roadNo = [];
                this.roadName = [];
                this.houseNo = [];
                this.houseName = [];
                this.apartmentNo = [];
                this.flatNo = [];
                this.area = [];
            }
            return Address;
        })
        .factory( "AddressFilterTypeIds", function() {
            function AddressFilterTypeIds(roadNoFilter, roadNameFilter, houseNoFilter, apartmentNoFilter, flatNoFilter, areaFilter) {
                this.houseNoFilter = houseNoFilter;
                this.roadNoFilter = roadNoFilter;
                this.roadNameFilter = roadNameFilter;
                this.flatNoFilter = flatNoFilter;
                this.apartmentNoFilter = apartmentNoFilter;
                this.areaFilter = areaFilter;
            }
            AddressFilterTypeIds.prototype.getJson = function() {
                var jsonData = {
                    "filterTypeId":{
                        "houseNo":this.houseNoFilter,
                        "roadNo":this.roadNoFilter,
                        "roadName":this.roadNameFilter,
                        "apartmentNo":this.apartmentNoFilter,
                        "flatNo":this.flatNoFilter,
                        "area":this.areaFilter
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
            AddressFilterTypeIds.prototype.resetFilterTypeIds = function() {
                this.houseNoFilter = null;
                this.roadNoFilter = null;
                this.roadNameFilter = null;
                this.flatNoFilter = null;
                this.apartmentNoFilter = null;
                this.areaFilter = null;
            }
            return AddressFilterTypeIds;
        })
        .$inject = ['$scope', 'addressSharedService'];
})(window.angular)
