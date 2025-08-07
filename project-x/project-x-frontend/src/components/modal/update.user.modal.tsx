import { App, Input, Modal } from "antd";
import { useEffect, useState } from "react";
import { updateUserAPI } from "../../services/api";

interface IUser {
  id: number;
  name: string;
  email: string;
}

interface IProps {
  isOpenUpdateModal: boolean;
  setIsOpenUpdateModal: (v: boolean) => void;
  fetchUsers: any;
  userUpdate: IUser | null;
  setUserUpdate: any;
}

const UpdateUserModal = (props: IProps) => {
  const { notification, message } = App.useApp();
  const {
    isOpenUpdateModal,
    setIsOpenUpdateModal,
    fetchUsers,
    userUpdate,
    setUserUpdate,
  } = props;
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);

  useEffect(() => {
    if (userUpdate) {
      setName(userUpdate.name);
      setEmail(userUpdate.email);
    }
  }, [userUpdate]);

  const handleSubmit = async () => {
    setIsLoading(true);
    if (userUpdate) {
      try {
        const res = await updateUserAPI(userUpdate?.id, name, email);
        if (res.data.data) {
          message.success("Cập nhật thành công!");
        }
      } catch (error: any) {
        const m = error?.response?.data?.message ?? "Unknown";
        notification.error({
          message: "Có lỗi xảy ra!",
          description: m,
        });
      } finally {
        setIsOpenUpdateModal(false);
        setName("");
        setEmail("");
        await fetchUsers();
      }
    }
    setIsLoading(false);
  };

  return (
    <Modal
      maskClosable={false}
      title="Update A User"
      open={isOpenUpdateModal}
      onOk={handleSubmit}
      onCancel={() => {
        setIsOpenUpdateModal(false);
        setUserUpdate(null);
      }}
      okText={"Update"}
      okButtonProps={{ loading: isLoading }}
    >
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          gap: 10,
          marginBottom: 10,
        }}
      >
        <span>Name:</span>
        <Input value={name} onChange={(v) => setName(v.target.value)} />
      </div>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          gap: 10,
          marginBottom: 10,
        }}
      >
        <span>Email:</span>
        <Input value={email} onChange={(v) => setEmail(v.target.value)} />
      </div>
    </Modal>
  );
};

export default UpdateUserModal;
