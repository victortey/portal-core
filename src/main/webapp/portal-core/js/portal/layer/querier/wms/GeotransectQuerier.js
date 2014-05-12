/**
 *
 * Function class to handle Geotransect WMS. Method are exposed as public static method as they represent functions rather then Objects.
 */

Ext.define('portal.layer.querier.wms.GeotransectQuerier', {
    extend : 'portal.layer.querier.wms.WMSQuerier',

    GEOTRANSECTDATASERVICE : '/geotransect-dataservices',
    GETSEISMICSECTIONS : "/getSeismicSections.json?",
    GETSEGYDATASETS : "/getSEGYDatasets.json?",

    constructor : function(config) {
        this.callParent(arguments);
    },


   /**
    * Parse the response and retrieve the CSWRecord for that point.
    */
    _getCSWRecord : function(cswrecordurl,queryTarget,callback) {

        var cswRecordStore = Ext.create('Ext.data.Store', {
            id:'seismicCSWRecordStore',
            autoLoad: false,
            model : 'portal.csw.CSWRecord',
            proxy: {
                type: 'ajax',
                url: 'getSeismicCSWRecord.do',
                extraParams: {
                    service_URL : cswrecordurl
                },
                reader: {
                    type: 'json',
                    root: 'data',
                    successProperty: 'success',
                    totalProperty: 'totalResults'
                }

            }

        });

        cswRecordStore.load({
            scope: this,
            callback: function(records, operation, success) {
                if(success){
                    var panel = Ext.create('portal.layer.querier.BaseComponent', {
                        border : false,
                        autoScroll : true,
                        items : [{
                            xtype : 'cswmetadatapanel',
                            border : false,
                            cswRecord : records[0]
                        }]
                    });

                    callback(this, [panel], queryTarget);
                }else{
                    callback(this, [this.generateErrorComponent('There was an error when attempting to contact the remote WMS instance for information about this point.')], queryTarget);
                }
            }
        });
    },

    /**
     * See parent class for definition
     *
     * Makes a WMS request, waits for the response and then parses it passing the results to callback
     */
    query : function(queryTarget, callback) {
        var proxyUrl = this.generateWmsProxyQuery(queryTarget, 'application/vnd.ogc.gml');
        this._getCSWRecord("http://www.ga.gov.au/metadata-gateway/metadata/record/gcat_74423/xml",queryTarget,callback)

    }

});
