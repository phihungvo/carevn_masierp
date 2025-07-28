import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IUniformImport } from 'app/shared/model/report.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import React from 'react';
import { useMemo } from 'react';
import reportUniformsExportsMapping from './report-uniforms-exports-mapping';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { reportUniformsExportsTypeColorMapping, reportUniformsExportsTextMapping } = reportUniformsExportsMapping;

export const generateColumns = (
  toggle: () => void,
  setSelectedRecord: (id: string) => void,
  setType: (type: UNIFORM_REPORT_TYPE) => void,
): ColumnsTypes<IUniformImport> => {
  const handleDetail = (id: string, type: UNIFORM_REPORT_TYPE) => {
    setSelectedRecord(id);
    setType(type);
    toggle();
  };

  const columns: ColumnsTypes<IUniformImport> = useMemo(() => {
    return [
      {
        title: 'Loại đồng phục',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph text={
              record?.type === UNIFORM_REPORT_TYPE.STOCK ? (
                text
              ) : (
                <p className="attachment-link" onClick={() => handleDetail(record.id, record.type as UNIFORM_REPORT_TYPE)}>
                  {text}
                </p>
              )
            } id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Số lượng',
        key: 'total',
        dataIndex: 'total',
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Trạng thái',
        key: 'type',
        dataIndex: 'type',
        render: text => text && <Badge color={reportUniformsExportsTypeColorMapping(text)}>{reportUniformsExportsTextMapping(text)}</Badge>,
      },
    ];
  }, []);

  return columns;
};
