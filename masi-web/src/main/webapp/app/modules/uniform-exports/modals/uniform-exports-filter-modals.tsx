import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useCallback, useRef, useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import DatePicker from 'app/components/date-picker/date-picker';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { UNIFORM_RELEASE_TYPE } from 'app/shared/model/enumerations/uniform.model';
import { IUniformReleaseParams } from 'app/shared/model/uniform.model';
import useUniform from 'app/hooks/use-uniform';
import Select from 'app/components/select/select';
import { BaseOption } from 'app/shared/model/pagination.model';
import uniformMapping from 'app/modules/uniform/uniform-mapping';

const { uniformReleaseTypeMapping } = uniformMapping;
const { useUniforms } = useUniform;

interface IUniformExportsFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IUniformReleaseParams>>;
}

const UniformExportsFilterModals = (props: IUniformExportsFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [type, setType] = useState<UNIFORM_RELEASE_TYPE[]>([]);
  const [uniformId, setUniformId] = useState<string[]>([]);
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const { data, isLoading } = useUniforms();

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      uniformId,
      type,
      startDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      endDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setType([]);
    setUniformId([]);
    setSelectedDate([]);
    setFilter(prev => ({
      ...prev,
      type: [],
      uniformId: [],
      startDate: undefined,
      endDate: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const uniformOptions = data?.data?.map(u => ({
    label: u?.name,
    value: u?.id,
  }));

  const closeExportCalendar = () => {
    setSelectedDate(null)
    datePickerRef?.current?.closeCalendar()
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="orders-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Row>
          <Col md={6}>
            <FormGroup>
              <Label htmlFor="exportType">Loại xuất</Label>
              <Input id="exportType" name="exportType" type="select" onChange={e => setType([e.target.value as UNIFORM_RELEASE_TYPE])}>
                <option selected disabled>
                  Chọn loại xuất
                </option>
                <option value={UNIFORM_RELEASE_TYPE.SALE}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SALE)}</option>
                <option value={UNIFORM_RELEASE_TYPE.SUPPORT}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SUPPORT)}</option>
                <option value={UNIFORM_RELEASE_TYPE.SENIORITY}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.SENIORITY)}</option>
                <option value={UNIFORM_RELEASE_TYPE.OTHER}>{uniformReleaseTypeMapping(UNIFORM_RELEASE_TYPE.OTHER)}</option>
              </Input>
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup>
              <Label htmlFor="type">Loại đồng phục</Label>

              <Select<BaseOption>
                id="uniformId"
                name="uniformId"
                placeholder="Chọn loại đồng phục"
                options={uniformOptions}
                value={uniformOptions?.filter((option: any) => option.value === uniformId?.[0])}
                onChange={value => setUniformId([value?.value as string])}
                isLoading={isLoading}
              />
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup>
              <Label htmlFor="date">Ngày</Label>
              <DatePicker
                value={selectedDate}
                ref={datePickerRef}
                onChange={onChangeSelectedDate}
                id="date"
                name="date"
                arrow={false}
                range
                closeExportCalendar={closeExportCalendar}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default UniformExportsFilterModals;
