import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import useGroup from 'app/hooks/use-group';
import AuthoritiesList from 'app/modules/authorities-group/authorities-list';
import { IAuthorityParams } from 'app/shared/model/authority.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IGroupMutation } from 'app/shared/model/group.model';
import { groupSchema, GroupSchema } from 'app/validation/group.validation';
import React, { useEffect, useRef, useState } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Input, Row } from 'reactstrap';
import { AuthoritiesListConverted } from '../utils/format-data';
import { set } from 'lodash';
import TableV2 from 'app/components/table-v2/Table';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { useAuthoritiesConverted } from 'app/hooks/use-authority';
import { permissions } from 'app/config/permission';

const { usePostGroup, usePatchGroup, useGroupById } = useGroup;

interface IAuthoritiesGroupFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  formSearchText?: string;
  setFormSearchText?: React.Dispatch<React.SetStateAction<string>>
  formFilter?: IAuthorityParams
  setFormFilter?: React.Dispatch<React.SetStateAction<IAuthorityParams>>
  permissionList?: AuthoritiesListConverted
  setPermissionList?: React.Dispatch<React.SetStateAction<AuthoritiesListConverted>>
  permissionListTmpRef?: React.MutableRefObject<AuthoritiesListConverted>
  permissionId?: React.MutableRefObject<Set<string>>
}

const AuthoritiesGroupForm = (props: IAuthoritiesGroupFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord } = props;

  const methods = useForm<GroupSchema>({
    resolver: zodResolver(groupSchema),
  });

  const { control, setValue, handleSubmit } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
  };

  const { data: detail } = useGroupById(selectedRecord);
  const { data: permissionList } = useAuthoritiesConverted({
    size: DEFAULT_PAGE_SIZE_NAX, page: DEFAULT_PAGE
  });

  const [dataPermission, setDataPermission] = useState([]);

  useEffect(() => {
    const listPermission = permissions?.sort((a, b) => a.key.localeCompare(b.key))?.map(item => {
      return {
        key: item.key,
        name: item.name,
        permissions: {},
      }
    })
    setDataPermission(listPermission)
  }, [])

  useEffect(() => {
    const mapSelect = {}
    detail?.authorities?.map(author => {
      let keyArr = author.id.split(".")
      let key = keyArr[keyArr.length - 2];
      let action = keyArr[keyArr.length - 1];
      if(!(key in mapSelect)) {
        mapSelect[key] = {}
      }
      mapSelect[key][action] = true;
    })
    const listPermission = permissions?.sort((a, b) => a.name.localeCompare(b.name))?.map(item => {
      let select = {
        'VIEW': false,
        'CREATE': false,
        'EDIT': false,
        'EXPORT': false,
      }
      if(item.key in mapSelect) {
        select = mapSelect[item.key];
      }
      return {
        key: item.key,
        name: item.name,
        permissions: {
          'VIEW': select['VIEW'] ?? false,
          'CREATE': select['CREATE'] ?? false,
          'EDIT': select['EDIT'] ?? false,
          'EXPORT': select['EXPORT'] ?? false,
        },
      }
    })
    setDataPermission(listPermission)
  }, [detail])


  const setValueCheck = (key, action, value) => {
    const dataPermissionTmp = [...dataPermission]
    const permission = dataPermissionTmp.find(item => item.key === key)
    permission.permissions[action] = value
    setDataPermission(dataPermissionTmp)
  }

  const { mutate: update } = usePatchGroup(selectedRecord, onOkSuccess);
  const { mutate: create } = usePostGroup(onOkSuccess);

  const onSubmit: SubmitHandler<GroupSchema> = values => {
    const permissionSelected = [];
     dataPermission.filter(item=> {
       const permis = item.permissions
       const select = Object.keys(permis).filter(key => permis[key])
       if (select.length > 0) {
        select.forEach(key => {
          permissionSelected.push(`PERMISSION.${item.key}.${key}`)
        })
       }
    })
    console.log("permissionSelected",permissionSelected)
    const submitValues: IGroupMutation = {
      name: values.name,
      description: values.description,
      authorities: permissionSelected,
    };
    if (type === 'update') {
      update(submitValues);
      return;
    }
    create(submitValues);
  };

  useEffect(() => {
    if (detail) {
      setValue('name', detail.name);
      setValue('description', detail.description);
      setValue("authorities", detail.authorities);
    } else {
      setValue("name", "");
      setValue("description", "");
      setValue("authorities", [])
    }
  }, [detail]);

  const columns = [
    {
      header: {
        render: 'Quyền'
      },
      body: {
        render: ({ data, index }) => data.name,
      }
    },
    {
      header: {
        render: 'Xem'
      },
      body: {
        render: ({ data, index }) => {
          return (<Input
            id="isInvoice"
            placeholder="Chọn"
            type="checkbox"
            checked={data['permissions']['VIEW'] ?? false}
            onChange={e => {
              if (e.target.checked) {
                setValueCheck(data.key, 'VIEW', true);
              } else {
                setValueCheck(data.key, 'VIEW', false);
                setValueCheck(data.key, 'CREATE', false);
                setValueCheck(data.key, 'EDIT', false);
                setValueCheck(data.key, 'EXPORT', false);
              }
            }}
          />)
        },
      }
    },
    {
      header: {
        render: 'Tạo'
      },
      body: {
        render: ({ data, index }) => {
          return (<Input
            id="isInvoice"
            placeholder="Chọn"
            type="checkbox"
            checked={data['permissions']['CREATE'] ?? false}
            onChange={e => {
              if (e.target.checked) {
                setValueCheck(data.key, 'VIEW', true);
                setValueCheck(data.key, 'CREATE', true);
              } else {
                setValueCheck(data.key, 'CREATE', false);
              }
            }}
          />)
        },
      }
    },
    {
      header: {
        render: 'Cập nhật'
      },
      body: {
        render: ({ data, index }) => {
          return (<Input
            id="isInvoice"
            placeholder="Chọn"
            type="checkbox"
            checked={data['permissions']['EDIT'] ?? false}
            onChange={e => {
              if (e.target.checked) {
                setValueCheck(data.key, 'VIEW', true);
                setValueCheck(data.key, 'EDIT', true);
              } else {
                setValueCheck(data.key, 'EDIT', false);
              }
            }}
          />)
        },
      }
    },
    {
      header: {
        render: 'Export / Print'
      },
      body: {
        render: ({ data, index }) => {
          return (<Input
            id="isInvoice"
            placeholder="Chọn"
            type="checkbox"
            checked={data['permissions']['EXPORT'] ?? false}
            onChange={e => {
              if (e.target.checked) {
                setValueCheck(data.key, 'VIEW', true);
                setValueCheck(data.key, 'EXPORT', true);
              } else {
                setValueCheck(data.key, 'EXPORT', false);
              }
            }}
          />)
        },
      }
    },
  ];

  return (
    <FormProvider {...methods}>
      <Form id={FORM.GROUP} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="name" name="name" label="Tên nhóm quyền" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="description" name="description" label="Mô tả" />
          </Col>
        </Row>

        <div className="divider" />

        <TableV2<any>
          table_id={'permission-group'}
          columns={columns}
          data={dataPermission}
        />
      </Form>
    </FormProvider>
  );
};

export default AuthoritiesGroupForm;
