import { App, Input, Modal } from "antd";
import { useState } from "react";
import { createUserAPI } from "../../services/api";

interface IProps {
  isModalOpen: boolean;
  setIsModalOpen: (v: boolean) => void;
  fetchUsers: any;
}

const CreateUserModal = (props: IProps) => {
  const { notification, message } = App.useApp();
  const { isModalOpen, setIsModalOpen, fetchUsers } = props;
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      const res = await createUserAPI(name, email);
      if (res.data.data) {
        message.success("Tạo mới thành công!");
      }
    } catch (error: any) {
      const m = error?.response?.data?.message ?? "Unknown";
      notification.error({
        message: "Có lỗi xảy ra!",
        description: m,
      });
    } finally {
      setIsModalOpen(false);
      setName("");
      setEmail("");
      await fetchUsers();
    }
    setIsLoading(false);
  };

  return (
    <Modal
      maskClosable={false}
      title="Create A User"
      open={isModalOpen}
      onOk={handleSubmit}
      onCancel={() => setIsModalOpen(false)}
      okText={"Save"}
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

export default CreateUserModal;
