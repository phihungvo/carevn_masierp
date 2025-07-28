import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import Input from 'app/components/input/input';
import { Typography } from 'app/components/typography/typography';
import { clone } from 'lodash';
import { useEffect, useState } from 'react';
import { FieldError, FieldErrorsImpl, Merge } from 'react-hook-form';

interface TabTableVerifyProps {
  title: string;
  rows: string[];
  name?:
    | 'additives.attributes'
    | 'rawMaterial.attributes'
    | 'production.monitorMachineOperation.furnace'
    | 'production.monitorMachineOperation.dryingFurnace'
    | 'production.checkMagnetGrid.magnet'
    | 'production.checkMagnetGrid.floorGrid'
    | 'production.checkMagnetGrid.grindingGrid';
  disabled?: boolean;
  onChange: (values) => void;
  data: { title?: string; pass?: boolean; fail?: boolean; reason?: string }[];
  errors?: Merge<
    FieldError,
    Merge<
      FieldError,
      FieldErrorsImpl<{
        title: string;
        pass: boolean;
        fail: boolean;
        reason: string;
      }>
    >[]
  >;
}
export const TabTableVerify = (props: TabTableVerifyProps) => {
  const { title, rows, name, disabled, onChange, data, errors } = props;

  const [result, setResult] = useState([]);

  const onChecked =
    (key: string, key2: string, index: number) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const isChecked = e.target.checked;
      const tmp = clone(result);
      tmp[index][key] = isChecked;
      tmp[index][key2] = !isChecked;
      setResult(tmp);

      onChange(tmp);
    };

  const onChangeReason =
    (key: string, index: number) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;
      const tmp = clone(result);
      tmp[index][key] = value;
      setResult(tmp);

      onChange(tmp);
    };

  useEffect(() => {
    if (rows.length > 0) {
      if (name) {
        const resultTmp = rows?.map(x => {
          const selected = (data ?? [])?.find(e => e?.title === x);
          if (selected) return { ...selected };
          else return { title: x, pass: false, fail: false, reason: '' };
        });
        setResult(resultTmp);
      } else {
        const resultTmp = rows?.map(x => {
          const selected = (result ?? [])?.find(e => e?.title === x);
          if (selected) return { ...selected };
          else return { title: x, pass: false, fail: false, reason: '' };
        });
        setResult(resultTmp);
      }
    }
  }, [rows]);

  return (
    <Flex
      direction="column"
      style={{
        borderRadius: '16px',
        border: '1px solid #D0D5DD',
        overflow: 'hidden',
      }}
    >
      <Typography
        level={6}
        style={{
          background: '#D0D5DD',
          color: '#344054',
          fontWeight: '500',
          height: '32px',
          display: 'flex',
          alignItems: 'center',
          paddingLeft: '16px',
        }}
      >
        {title}
      </Typography>
      <table style={{ margin: '0px 16px' }}>
        <colgroup>
          <col style={{ width: '20%' }} />
          <col style={{ width: '7.5%' }} />
          <col style={{ width: '10%', textWrap: 'nowrap' }} />
          <col style={{ width: '65%' }} />
        </colgroup>
        <thead>
          <tr style={{ height: '40px' }}>
            <th>#</th>
            <th style={{ textAlign: 'center' }}>Đạt</th>
            <th style={{ textAlign: 'center', textWrap: 'nowrap' }}>
              Không đạt
            </th>
            <th style={{ paddingLeft: '20px' }}>Lý do</th>
          </tr>
        </thead>
        <tbody>
          {rows?.map((x, idx) => (
            <>
              <tr key={idx} style={{ padding: '0px 0px 8px' }}>
                <td>
                  {x}
                  {(errors?.message || errors?.[idx]?.['title']?.message) && (
                    <div style={{ padding: '8px 0px', display: 'block' }}>
                      <FormError
                        message={
                          errors?.message || errors?.[idx]?.['title']?.message
                        }
                      />
                    </div>
                  )}
                </td>
                <td style={{ textAlign: 'center' }}>
                  <Input
                    type="checkbox"
                    checked={result?.[idx]?.['pass']}
                    onChange={onChecked('pass', 'fail', idx)}
                    disabled={disabled}
                  />
                </td>
                <td style={{ textAlign: 'center' }}>
                  <Input
                    type="checkbox"
                    checked={result?.[idx]?.['fail']}
                    onChange={onChecked('fail', 'pass', idx)}
                    disabled={disabled}
                  />
                </td>
                <td style={{ paddingLeft: '20px' }}>
                  <FormInputV2
                    name=""
                    value={result?.[idx]?.['reason']}
                    onChange={onChangeReason('reason', idx)}
                    disabled={disabled}
                    errorMsg={errors?.[idx]?.['reason']?.message}
                  />
                </td>
              </tr>
            </>
          ))}
        </tbody>
      </table>
    </Flex>
  );
};
