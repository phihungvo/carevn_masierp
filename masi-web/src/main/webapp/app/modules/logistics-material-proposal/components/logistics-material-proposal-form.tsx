import React from 'react';
import { Col, Row } from 'reactstrap';
import { DateObject } from 'react-multi-date-picker';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormDatePicker from 'app/components/form/form-date-picker';

import useEmployee from 'app/hooks/use-employee';
import useWorkspace from 'app/hooks/use-workspace';

import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { logisticsMaterialProposalSchema, LogisticsMaterialProposalSchema } from 'app/validation/logistics-material-proposal';
import { IUnitOption } from 'app/shared/model/contract.model';
import UnitSelect from 'app/modules/logistics-material-proposal/components/unit-select';
import { DEFAULT_DECIMAL_REGEX } from 'app/constants/common';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';

interface ILogisticsMaterialProposalFormProps {
    type: 'create' | 'update';
    toggle?: () => void;
    toggleSuccess?: () => void;
    selectedRecord?: string | null;
    setSelectedRecord?: (value: string | null) => void;
}

const { useGetEmployeesQuery } = useEmployee;
const { useGetWorkspacesQuery } = useWorkspace;

const unitOption: IUnitOption[] = [
    {
        label: 'Kg',
        value: 'Kg',
    },
    {
        label: 'Tấn',
        value: 'Tấn',
    },
    {
        label: 'Cái',
        value: 'Cái',
    },
];



const LogisticsMaterialProposalForm = (props: ILogisticsMaterialProposalFormProps) => {
    const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

    const methods = useForm<LogisticsMaterialProposalSchema>({
        defaultValues: {
            voucherDate: new DateObject
        },
        resolver: zodResolver(logisticsMaterialProposalSchema),
    });

    const { control, setValue, handleSubmit, formState } = methods;

    const { data, isLoading } = useGetEmployeesQuery();
    const { data: workspaces, isLoading: wspLoading } = useGetWorkspacesQuery();


    const onSubmit: SubmitHandler<LogisticsMaterialProposalSchema> = values => {
        toggleSuccess()
    };

    // useEffect(() => {
    //     if (detail) {

    //     }
    // }, [detail]);

    return (
        <FormProvider {...methods}>
            <Form id={FORM.LOGISTICS_MATERIAL_PROPOSAL} onSubmit={handleSubmit(onSubmit)}>
                <Row>
                    <Col md={6}>
                        <FormInput control={control} id="package" name="numberVoucher" label="Số CT" disabled={type === 'update'} />
                    </Col>

                    <Col md={6}>
                        <FormDatePicker setValue={setValue} control={control} id="package" name="voucherDate" label="Ngày CT" disabled={type === 'update'} formState={formState} />
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <FormSelect
                            control={control}
                            id="employeeId"
                            name="employeeId"
                            placeholder="Chọn nhân viên"
                            label="Nhân viên"
                            options={data?.data?.map(e => ({
                                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                                value: e?.id,
                            }))}
                            isLoading={isLoading}
                        />
                    </Col>

                    <Col md={6}>
                        <FormSelect
                            disabled={type === 'update'}
                            control={control}
                            id="workspaceId"
                            name="workspaceId"
                            placeholder="Chọn BP/Nhà máy"
                            label="BP / Nhà máy"
                            options={workspaces?.data?.map(wsp => ({
                                label: wsp?.name,
                                value: wsp?.id,
                            }))}
                            isLoading={wspLoading}
                        />
                    </Col>

                </Row>

                <Row>
                    <Col md={6}>
                        <FormSelect
                            control={control}
                            id="supplier"
                            name="supplier"
                            placeholder="Chọn nhà cung cấp"
                            label="Nhà cung cấp"
                            options={data?.data?.map(e => ({
                                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                                value: e?.id,
                            }))}
                            isLoading={isLoading}
                        />
                    </Col>

                    <Col md={6}>
                        <UnitSelect data={unitOption} label='Đơn vị' control={control} index={1} isLoading={isLoading} setValue={setValue} />
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <FormInput control={control} id="package" name="productName" label="Tên hàng hóa" />
                    </Col>

                    <Col md={6}>
                        <FormInput control={control} id="package" name="quantity" type='number' label="Số lượng" />
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <FormInput
                            control={control}
                            id="package"
                            name="unitPrice"
                            label="Đơn giá"
                            type='number'
                            nChange={e => handleValidDecimal<LogisticsMaterialProposalSchema>(e.target.value, 'unitPrice', DEFAULT_DECIMAL_REGEX, setValue)}
                            onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)} />
                    </Col>
                </Row>

                <Row>
                    <Col md={12}>
                        <FormInput control={control} id="package" type="textarea" name="note" label="Ghi chú" />
                    </Col>
                </Row>
            </Form>
        </FormProvider>
    );
};

export default LogisticsMaterialProposalForm;
