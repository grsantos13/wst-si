package br.com.gn.si.send

import br.com.gn.si.ShippingInstructions
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.EmailConstants
import org.apache.commons.mail.HtmlEmail
import javax.inject.Singleton

@Singleton
class MailSender() {
    companion object {

        fun send(
            subject: String,
            shippingInstructions: ShippingInstructions,
            exporterIsManufacturer: Boolean,
            vararg to: String
        ) {
            sender().run {
                to.forEach { addTo(it) }
                setSubject(subject)
                attach(EmailAttachment().apply {
                    path = "C:\\Users\\Gustavo.Santos\\Documents\\git\\wst\\wstsi\\Pallets Pictures.pdf"
                    disposition = EmailAttachment.ATTACHMENT
                    description = "Pallets Pictures"
                    name = "Pallets Pictures.pdf"
                })
                attach(EmailAttachment().apply {
                    path = "C:\\Users\\Gustavo.Santos\\Documents\\git\\wst\\wstsi\\Shipping Instruction Terms.pdf"
                    disposition = EmailAttachment.ATTACHMENT
                    description = "Shipping Instruction Terms"
                    name = "Shipping instruction terms.pdf"
                })
                setHtmlMsg(message(shippingInstructions, exporterIsManufacturer))
                this.send()
            }
        }


        private fun message(si: ShippingInstructions, exporterIsManufacturer: Boolean): String {

            val manufacturer = if (exporterIsManufacturer) {
                """
                    <tr>
                        <td colspan="6" rowspan="4">Manufacturer (Full Details)</td>
                        <td colspan="6" style="border-bottom: none;">${si.exporter.exporterName}</td>
                    </tr>
                    <tr><td colspan="6" style="border-top: none; border-bottom: none;">${si.exporter.exporterStreet}</td></tr>
                    <tr><td colspan="6" style="border-top: none; border-bottom: none;">${si.exporter.exporterCityAndZipCode}</td></tr>
                    <tr><td colspan="6" style="border-top: none;">${si.exporter.exporterCountry}</td></tr>
                """.trimIndent()
            } else {
                """
                <td colspan ="6">Manufacturer (Full Details)</td>
                <td colspan ="6">To be confirmed by the exporter</td>
                """.trimIndent()
            }

            val materials = si.materials.joinToString { item ->
                """
                    <tr>
                    <td colspan ="1">${item.code}</td>
                    <td colspan ="6">${item.description}</td>
                    <td colspan ="1">${item.unitPrice}</td>
                    <td colspan ="1">${si.exporter.exporterCurrency}</td>
                    <td colspan ="1">${item.ncm}</td>
                    <td colspan ="1">${item.quantity}</td>
                    <td colspan ="1">${item.unitPrice.multiply(item.quantity).setScale(2)}</td>
                    </tr>
                """.trimIndent()
            }

            return """
                <head>
                    <title>SI</title>
                    <meta charset="UTF-8" />
                    <style>
                        .block {
                            display: block;
                        }
                        .red {
                            color: red;
                        }
                        .underlined {
                            text-decoration: underline;
                        }
                        .bold {
                            font-weight: bold;
                        }
                        table, th, td {
                            border: 1px solid black;
                            border-collapse: collapse;
                        }
                        th, td {
                            padding: 5px;
                            text-align: left;
                        }
                    </style>
                </head>
                <body>
                <p> Dears, </p>
                <p> Find below the main information required for the PO ${si.orderNumber} and attached the shipping instruction with procedures and Brazilian Customs rules .
                To issue BL include the number of order according to instruction below and include also the freetime of demurrage at destination.</p>
                <p> You’ll also find attached the photos that show how pallets must be to be accepted by Brazilian customs.</p>
                <p> <b style = "background-color: yellow;"> MANDATORY INFORMATION : </b> <br/>
                The Unilever Brazil PO number must be entered in the specific field below at the time of booking request.Find below the specific field for each agent :
                <span class="block"><b>Hapag Lloyd:</b> Consignee Reference</span>
                <span class="block"><b>Hamburg SUD:</b> Exporter Reference</span>
                <span class="block"><b>Maersk:</b> Invoice Reference Number</span>
                <span class="block"><b>MSC:</b> Shipper's Reference Number</span>
                <span class="block"><b>CMA:</b> Shipper's Reference Number</span>
                </p>
                <p> In case the cargo is shipped with an agent not specified above, please, insert the PO number where it'll be reflected in service bills.</p>

                <table style ="width: 100%">
                <thead> <tr> <th colspan ="12" style = "text-align: center;">PURCHASE ORDER ${si.orderNumber} </th> </tr> </thead>
                <tbody>
                <tr>
                <td rowspan ="4" colspan = "6">Importer /Buyer (Full Details)</td>
                <td colspan ="6" style = "border-bottom: none;">${si.importer.importerFiscalName} </td>
                </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none; border-bottom: none;">CNPJ ${si.importer.importerFiscalNumber} </td> </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none; border-bottom: none;">${si.importer.importerStreetAndZipCode}</td> </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none;">${si.importer.importerCityAndCountry} </td> </tr>
                <tr>
                <td colspan ="6" rowspan = "4">Exporter (Full Details)</td>
                <td colspan ="6" style = "border-bottom: none;">${si.exporter.exporterName} </td>
                </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none; border-bottom: none;">${si.exporter.exporterStreet} </td> </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none; border-bottom: none;">${si.exporter.exporterCityAndZipCode} </td> </tr>
                <tr> <td colspan =
                    "6" style ="border-top: none;">${si.exporter.exporterCountry} </td> </tr>
                <tr>
                $manufacturer
                </tr>
                <tr> <th colspan = "12" style ="text-align: center;">MATERIAL INFORMATION:</th></tr>
                <tr>
                <td colspan ="1">Code</td>
                <td colspan ="6">Description</td>
                <td colspan ="1">Quantity</td>
                <td colspan ="1">Currency</td>
                <td colspan ="1">NCM Code</td>
                <td colspan ="1">Unit price</td>
                <td colspan ="1">Total price</td>
                </tr>
                $materials
                <tr>
                <td colspan ="6">Incoterm</td>
                <td colspan ="6">${si.incoterm}</td>
                </tr>
                <tr>
                <td colspan ="6">Payment Terms</td>
                <td colspan ="6">${si.paymentTerms}</td>
                </tr>
                <tr>
                <td colspan ="6">Import License Required Before Shipment?</td>
                <td colspan ="6">${if (si.preShipmentLicense) "Required" else "Not Required"}</td>
                </tr>
                <tr>
                <td colspan ="6">Freight Modal</td>
                <td colspan ="6">${si.modal}</td>
                </tr>
                <tr>
                <td colspan ="6">Shipowner /Freight Forwarder and Contact</td>
                <td colspan ="6">${si.freightForwarder}</td>
                </tr>
                <tr>
                <td colspan ="6">Airport /Border /Port of Origin</td>
                <td colspan ="6">${si.originPort}</td>
                </tr>
                <tr>
                <td colspan ="6">Airport /Border /Port of Destination</td>
                <td colspan ="6">${si.destinationPort}</td>
                </tr>
                <tr>
                <td colspan ="6">Availability of Goods Until</td>
                <td colspan ="6">TBC</td>
                </tr>
                <tr>
                <td colspan ="6">ETD at Origin Airport /Border /Port</td>
                <td colspan ="6">TBC</td>
                </tr>
                <tr>
                <td colspan ="6">ETA at Destination Airport /Border /Port</td>
                <td colspan ="6">TBC</td>
                </tr>
                <tr>
                <td colspan ="6" rowspan = "3">Contact person and full address to sending the original documents</td>
                <td colspan ="6" style = "border-bottom: none;">${si.responsible}</td>
                </tr>
                <tr>
                <td colspan ="6" style = "border-bottom: none; border-top: none;">Address: Av. Mercedes Benz 170 – Distrito Industrial – Campinas – São Paulo – Brazil – CEP 130054-750</td>
                </tr>
                <tr>
                <td colspan ="6" style = "border-top: none;">${si.emails}</td>
                </tr>
                </tbody>

                </table>

                <p>
                <span class="block red underlined bold">NEW BRAZILIAN LAW, FOR WOODEN PACKAGES:</span>
                <span class="block">From February 2016 is not authorized to use any kind of wooden package, pallets or wooden support, not properly treated, certificate and stamped according to NIMF15 International Rules and Phytosanitary Procedures, otherwise the cargo will be subject to rejection. Please read carefully the instructions in attachment.</span>
                <span class="block">If the presence of live pests is detected or any active infestation of them, BRAZILIAN CUSTOMS, can demand return of package  or ALL material, back to origin country, (Article nr. 32 of IN in reference), and additional costs.</span>
                <span class="block red underlined bold" style = "margin-top: 5px">NEW INTERNATIONAL MARITIME REGULAMENTATION-VERIFIED GROSS MASS-EFFECTIVE FROM 1ST JULY 2016</span>
                <span class="block">Effective 1st Jul 2016, new mandatory operational requirement prescribed by International Maritime organization (IMO). Exporters have  to provide the gross weight of the container full to the shipping line prior to loading of the containers. The shipping Line/Port & Terminal Authority will not load containers that are moved into port without the Gross Weight being declaration from shipper.</span>
                </p>
                <p>
                <span class="block red underlined bold"><span style = "color: black; text-decoration: underline;">IMPORTANT: </span>Please work with your respective local provider to ensure compliance of the new regulation to avoid containers from being stopped from loading.</span>
                <span class="block red underlined bold" style = "margin-top: 5px;"><span style = "color: black; text-decoration: underline;">IMPORTANT: </span>Please make sure that these instructions above and in attachment document are being followed properly, otherwise we may be liable for fines by customs. In this case we understand that the cost of the fine should not be paid by UNILEVER BRASIL LTDA and that will be passed to your company through Unilever.</span>
                </p>
                <p> Thank you !</p>

                </body>
                """.trimIndent()
        }

        private fun sender(): HtmlEmail {
            val sender = HtmlEmail()
            sender.setCharset(EmailConstants.UTF_8)
            sender.hostName = "smtp.gmail.com";
            sender.setSmtpPort(587);
            sender.isSSLOnConnect = true;
            sender.setAuthentication("springwebtest2@gmail.com", "ssbzynijkiurshie");
            sender.isStartTLSEnabled = true
            sender.isStartTLSRequired = true
            sender.setFrom("springwebtest2@gmail.com")
            return sender
        }
    }

}