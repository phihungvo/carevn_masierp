import BarChart from 'app/components/chart/bar-chart';
import { IUniformSupport } from 'app/shared/model/report.model';
import { generateRandomHexColorWithAlpha } from 'app/shared/util/generate-random-hex';
import React, { useMemo } from 'react';

interface IReportUniformsReimbursement {
  data: IUniformSupport[];
}

const ReportUniformsReimbursementChart = (props: IReportUniformsReimbursement) => {
  const { data } = props;

  const dataMap = useMemo(() => {
    const map = new Map<string, number>();
    data?.forEach(data => {
      map.set(data.name, map.get(data.name) ? map.get(data.name) + data.total : data.total);
    });
    return Array.from(map.entries()).map(([key, value]) => ({ name: key, total: value }));
  }, [data]);

  const randomColor = dataMap?.map(() => generateRandomHexColorWithAlpha());

  const chartData = {
    labels: dataMap?.map(data => data?.name) || [],
    datasets: [
      {
        label: 'Số lượng',
        data: dataMap?.map(data => data?.total) || [],
        backgroundColor: randomColor?.map(c => c.backgroundColor),
        maxBarThickness: 64,
        borderWidth: 1,
        borderColor: randomColor?.map(c => c.borderColor),
        borderRadius: 4,
      },
    ],
  };

  return (
    <div style={{ background: 'white', padding: 8 }}>
      <BarChart chartData={chartData} />
    </div>
  );
};

export default ReportUniformsReimbursementChart;
