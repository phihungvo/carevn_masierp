import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import FactoriesForm from './components/factories-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import FactoriesUpdateSuccessModals from './modals/factories-update-success-modals';

const FactoriesUpdate = () => {
  const navigate = useNavigate();

  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);

  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Cập nhật</Typography>
            <Flex align="center" gap={10}>
              <ButtonV2 onClick={() => navigate(PATH.FACTORIES)}>Đóng</ButtonV2>
              <ButtonV2
                color="blue"
                variant="solid"
                form={FORM.FACTORIES}
                type="submit"
              >
                Lưu
              </ButtonV2>
            </Flex>
          </Flex>
        }
      >
        <FactoriesForm type="update" toggleSuccess={toggleUpdateSuccess} />
      </CardV2>

      <FactoriesUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
    </>
  );
};

export default FactoriesUpdate;
