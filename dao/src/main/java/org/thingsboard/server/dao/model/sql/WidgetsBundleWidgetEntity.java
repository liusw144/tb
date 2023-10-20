/**
 * Copyright © 2016-2023 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.model.sql;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.thingsboard.server.common.data.id.WidgetTypeId;
import org.thingsboard.server.common.data.id.WidgetsBundleId;
import org.thingsboard.server.common.data.widget.WidgetsBundleWidget;
import org.thingsboard.server.dao.model.ToData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.WIDGETS_BUNDLE_WIDGET_TABLE_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.WIDGET_TYPE_ORDER_PROPERTY;

@Data
@Entity
@Table(name = WIDGETS_BUNDLE_WIDGET_TABLE_NAME)
@IdClass(WidgetsBundleWidgetCompositeKey.class)
public final class WidgetsBundleWidgetEntity implements ToData<WidgetsBundleWidget> {

    @Id
    @Column(name = "widgets_bundle_id", columnDefinition = "uuid")
    @Type(type = "uuid-char")
    private UUID widgetsBundleId;

    @Id
    @Column(name = "widget_type_id", columnDefinition = "uuid")
    @Type(type = "uuid-char")
    private UUID widgetTypeId;

    @Column(name = WIDGET_TYPE_ORDER_PROPERTY)
    private int widgetTypeOrder;

    public WidgetsBundleWidgetEntity() {
        super();
    }

    public WidgetsBundleWidgetEntity(WidgetsBundleWidget widgetsBundleWidget) {
        widgetsBundleId = widgetsBundleWidget.getWidgetsBundleId().getId();
        widgetTypeId = widgetsBundleWidget.getWidgetTypeId().getId();
        widgetTypeOrder = widgetsBundleWidget.getWidgetTypeOrder();
    }

    public WidgetsBundleWidgetEntity(UUID widgetsBundleId, UUID widgetTypeId, int widgetTypeOrder) {
        this.widgetsBundleId = widgetsBundleId;
        this.widgetTypeId = widgetTypeId;
        this.widgetTypeOrder = widgetTypeOrder;
    }

    @Override
    public WidgetsBundleWidget toData() {
        WidgetsBundleWidget result = new WidgetsBundleWidget();
        result.setWidgetsBundleId(new WidgetsBundleId(widgetsBundleId));
        result.setWidgetTypeId(new WidgetTypeId(widgetTypeId));
        result.setWidgetTypeOrder(widgetTypeOrder);
        return result;
    }
}
