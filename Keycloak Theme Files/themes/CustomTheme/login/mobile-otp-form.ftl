<#import "template.ftl" as layout>
<@layout.registrationLayout; section>

    <#if section = "header">
        ${msg("Please Enter OTP")}
    <#elseif section = "form">
        <style>
            .resend-link {
                cursor: pointer;
                color: #0088ce;
                text-align: center;
            }

            .mb-30 {
                margin-bottom: 30px;
            }
        </style>
        <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
		
            <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('otp',properties.kcFormGroupErrorClass!)}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="otp" class="${properties.kcLabelClass!}">OTP</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="otp" class="${properties.kcInputClass!}" name="otp"/>
                </div>
            </div>
   

            <div class="${properties.kcFormGroupClass!}">
                

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!} mb-30">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("Login")}"/>
                </div>
                
				<div onclick="registerUser()" class="resend-link">Resend OTP</a></div>
				<div onclick="registerUser()" class="resend-link">Edit Mobile Number</a></div>
             
            </div>
        </form>
			
        <script>

            function registerUser() {
                console.log("Request to register new user");
                document.getElementById("resend").value = "yes";
                document.getElementById("kc-register-form").submit();
            }
			
			function getOTP(){
			
				
			}

        </script>
    </#if>
</@layout.registrationLayout>
