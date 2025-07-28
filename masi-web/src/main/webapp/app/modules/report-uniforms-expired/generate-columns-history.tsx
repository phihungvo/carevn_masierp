import dayjs from 'dayjs';
import React, { useMemo } from 'react';

import Flex from "app/components/flex/flex";
import Badge from 'app/components/badge/badge';
import reportUniformsExpiredMapping from './report-uniforms-expired-mapping';
import { DATE_FORMAT } from 'app/constants/common';
import { ColumnsTypes } from 'app/components/table/table.d';
import {IUniformRelease} from "app/shared/model/uniform.model";

const {  reportUniformsExpiringHistoryTypeColorMapping, reportUniformsExpiringHistoryTypeMapping } = reportUniformsExpiredMapping;

export const generateColumnsHistory = (): ColumnsTypes<IUniformRelease> => {
  const columns: ColumnsTypes<IUniformRelease> = useMemo(() => {
    return [
      {
        title: 'Ngày tạo',
        key: 'createAt',
        dataIndex: 'createAt',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
      },
      {
        title: 'Chi tiết',
        key: 'detail',
        render: (_, record) =>
            <Flex direction='column' gap={12} key={record.id}>
              <div >
                {
                  record?.uniformFormDetails?.map((record) => {
                    return (
                      <div key={record?.id}>{`${record?.uniform?.name} - ${record?.quantity}`}</div>
                    )
                  })
                }
              </div>
            </Flex>
      },
      {
        title: 'Trạng thái',
        key: 'type',
        dataIndex: 'type',
        render: text =>
          text !== 'UNKNOWN' && (
            <Badge color={reportUniformsExpiringHistoryTypeColorMapping(text)}>{reportUniformsExpiringHistoryTypeMapping(text)}</Badge>
          ),
      }
    ];
  }, []);

  return columns;
};
