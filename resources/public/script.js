$(function() {

    EncryptionView = Backbone.View.extend({

        events: {
            'click #option-left':  'cryptView',
            'click #option-right':  'breakView',
            'click .titleContainer': 'homeView',
            'click #encryptButton': 'getEncrypted',
            'click #decryptButton': 'getDecrypted'
        },

        initialize: function() {
            console.log('initialised');
        },

        homeView: function() {
            console.log('homeView');
            $('#background').css('width', '57.5%');
            $('.titleContainer').css('margin-bottom', '50px');
            $('.leftFormContainer').css('visibility', 'hidden');
            $('.leftButtonContainer').css('visibility', 'hidden');
        },

        cryptView: function() {
            console.log('cryptView');
            $('#background').css('width', '100%');
            $('.titleContainer').css('margin-bottom', '10px');
            $('.leftFormContainer').css('visibility', 'visible');
            $('.leftButtonContainer').css('visibility', 'visible');
        },

        breakView: function() {
            console.log('breakView');
            $('#background').css('width', '0%');
            $('.titleContainer').css('margin-bottom', '10px');
            $('.leftFormContainer').css('visibility', 'hidden');
            $('.leftButtonContainer').css('visibility', 'hidden');
        },

        getEncrypted: function() {
            console.log('getEncrypted');
            var str = jQuery.param(
                { 
                    'text': $('#unencrypted').val(),
                    'keyText': $('key').val()
                }
            );
            $.ajax({
                url: '/encrypt-random?' + str,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                timeout: 5000,
                success: function(data) {
                    $('#encrypted').val(data.encrypted);
                    $('#key').val(data.keyText);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert('error ' + textStatus + " " + errorThrown);
                }
            });
        },

        getDecrypted: function() {
            console.log('getDecrypted');
            var str = jQuery.param(
                { 
                    'text': $('#encrypted').val(),
                    'keyText': $('#key').val(),
                }
            );
            $.ajax({
                url: '/decrypt?' + str,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                timeout: 5000,
                success: function(data) {
                    $('#unencrypted').val(data.decrypted);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert('error ' + textStatus + " " + errorThrown);
                }
            });
        }

    });

    var view = new EncryptionView({el: 'body'});
});