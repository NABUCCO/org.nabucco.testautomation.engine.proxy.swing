/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.nabucco.testautomation.engine.proxy.swing.process.ui.table;

import javax.swing.JTable;

import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;

/**
 * DefaultSwingTableMapper
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class DefaultSwingTableMapper implements SwingTableMapper {

    private JTable table;

    public DefaultSwingTableMapper(JTable table) {
        this.table = table;
    }

    @Override
    public void createPropertyList(PropertyList parentList) {

        for (int row = 0; row < table.getModel().getRowCount(); row++) {

            PropertyList rowMap = PropertyHelper.createPropertyList(String.valueOf(row));

            // hgruenwald: skip last invisible column.
            for (int col = 0; col < table.getModel().getColumnCount() - 1; col++) {

                String value = SwingComponentEventCreator.readText(table, row, col);
                PropertyHelper.add(PropertyHelper.createTextProperty(String.valueOf(col), value), rowMap);
            }
            PropertyHelper.add(rowMap, parentList);
        }
    }

}
