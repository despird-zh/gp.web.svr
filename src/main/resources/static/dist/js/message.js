/*! 
 * The message process handler
 * 
 * Copyright (c) 2014-2015
 * @author Gary diao
 */
;(function ($, window, undefined)
{
    "use strict";
    /*
     usage : define the elements to be used.
    */
    var ErrMessage = {  
        errmsg_count : 0,
        $errmsg_clear_btn : $('span[gpid=err-message-clear-btn]'),
        $errmsg_count : $('strong[gpid=err-message-count]'),
        $errmsg_list : $('ul[gpid=err-message-list]'),
        $errmsg_message_header : $('div[gpid=message-in-header]'),
        
        $errmsg_container : $('div[gpid="message-toggle-container"]'),
        
        $errmsg_item_template : $('#message-text-template'), // template is defined in header.jsp
        $errmsg_header_template : $('#message-header-template'),
        
        initial : function(){

            var _self = this;
            _self.$errmsg_clear_btn.bind('click', function(evt){
            
                ErrMessage.$errmsg_list.empty();                
                ErrMessage.$errmsg_count.html('0');
                ErrMessage.errmsg_count = 0;
                ErrMessage.$errmsg_message_header.html('<i class="fa fa-hand-peace-o fa-fw m-r-sm"></i>Welcome to Groupress ECM !!!');
                ErrMessage.$errmsg_container.find('div.message-toggle').trigger('click');
            });
            
            _self.$errmsg_list.find('span[gpid=err-message-remove-btn]').bind('click', ErrMessage.remove);          
            _self.$errmsg_list.find('span[gpid=err-message-more-btn]').bind('click', ErrMessage.toggleDetail);
            
            $('div[gpid="message-toggle-container"] ul.dropdown-menu').on("click", function(e) {
                e.stopPropagation();
            });
        }
    };
    
    /*
     usage : toggle the visibility of detail message of list item
    */
    ErrMessage.toggleDetail = function(evt){
        var _$self = $(this); // button
        var $detailmsg = _$self.siblings('ol'); // detail messages
        if($detailmsg.hasClass('hidden')){
            // show detail
            $detailmsg.removeClass('hidden');
            // switch button icon
            _$self.find('i').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
        }else{
            $detailmsg.addClass('hidden');
            _$self.find('i').removeClass('fa-angle-double-up').addClass('fa-angle-double-down');
        }
        
    };
    
    /*
    usage : remove the message item
    
    */
    ErrMessage.remove = function(evt){
    
        // this is the dom element
        var _$self = $(this);
        var $item = _$self.parent('a').parent('li');
        $item.remove();
        // update the count of message
        var count = ErrMessage.$errmsg_list.find('>li').length;
        ErrMessage.$errmsg_count.html(count);
        if(count == 0){
            ErrMessage.$errmsg_container.find('div.message-toggle').trigger('click');
        }
    };
    
    /*
     usage : append message to message list
     parameter 1 : message data
       message_data = {
        warning : true
        error : true
        info : true
        timeText : 5 min
        messageText : ssssssss,
        detailMessages : [{property, message},{}]
     }
     parameter 2 : inheader - message to be shown in header label
     parameter 3 : showdetail
    **/
    ErrMessage.appendMessage = function(message_data, inheader, showdetail){
        var _self = this;
        
        var template = _self.$errmsg_item_template.html();
        Mustache.parse(template);
        if(null == message_data.detailMessages){
            message_data.hasDetail = false;
        }else if(message_data.detailMessages.length > 0){
            message_data.hasDetail = true;
        }else {
            message_data.hasDetail = false;
        }
        var rendered = Mustache.render(template, message_data);

        _self.$errmsg_list.prepend(rendered);
        // message remove
        _self.$errmsg_list.find('span[gpid=err-message-remove-btn]').unbind('click');
        _self.$errmsg_list.find('span[gpid=err-message-remove-btn]').bind('click', ErrMessage.remove);
        // detail message show
        _self.$errmsg_list.find('span[gpid=err-message-more-btn]').unbind('click');
        _self.$errmsg_list.find('span[gpid=err-message-more-btn]').bind('click', ErrMessage.toggleDetail);
        // show in header
        if(inheader){
            var header_templ = _self.$errmsg_header_template.html();
            Mustache.parse(header_templ);
            var rendered = Mustache.render(header_templ, message_data);
            _self.$errmsg_message_header.html(rendered);
            
            if(showdetail){
                _self.$errmsg_container.find('div.message-toggle').trigger('click');            
                _self.$errmsg_list.find('li:first span[gpid=err-message-more-btn]').click();
            }
        }
        // update the count of message
        var count = _self.$errmsg_list.find('>li').length;
        _self.$errmsg_count.html(count);
    };
    
    /*
     * Append the result directly to the message container.
     * @param action_result - data structor map to Server ActionResult class
     * @param showdetail - show the detail dropdown message list or not
     */
    ErrMessage.appendResult = function(action_result,showdetail){
        var _self = this;
        var currDate = new Date();
        if(action_result.state == 'success'){
            if(action_result.detailmsgs == undefined || action_result.detailmsgs.length == 0){
                // show message.
                _self.appendMessage({
                    info : true,
                    timeText : currDate.toLocaleTimeString(),
                    messageText : action_result.message,
                    detailMessages : action_result.detailmsgs
                }, true, showdetail);
            }else{
                // show message.
                _self.appendMessage({
                    warning : true,
                    timeText : currDate.toLocaleTimeString(),
                    messageText : action_result.message,
                    detailMessages : action_result.detailmsgs
                }, true, showdetail);
            }
        }
        else{
            // show message.
            _self.appendMessage({
                error : true,
                timeText : currDate.toLocaleTimeString(),
                messageText : action_result.message,
                detailMessages : action_result.detailmsgs
            }, true,showdetail);
        }
    };

    ErrMessage.initial();
    
    // link object to global context, so as to for access.
    GPContext.AppendMessage = $.proxy(ErrMessage.appendMessage, ErrMessage);
    
    GPContext.AppendResult = $.proxy(ErrMessage.appendResult, ErrMessage);

})(jQuery, window);


